package storm.demo;

import java.util.Map;
import java.util.Random;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This topology demonstrates Storm's stream groupings and multilang capabilities.
 */
public class SampleTopology {
	
	public static class SampleSpout extends BaseRichSpout {
		  String[] sentences = new String[]{ "1", "2", "3", "4" };
		  SpoutOutputCollector _collector;
		  Random _rand;


		  @Override
		  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		    _collector = collector;
		    _rand = new Random();
		  }

		  @Override
		  public void nextTuple() {
		    Utils.sleep(1000);
		    String sentence = sentences[_rand.nextInt(sentences.length)];
		    if("4".equals(sentence)){
		    	sentences = new String[]{ "1", "2", "3"};
		    }
		    _collector.emit(new Values(sentence), sentence);
		  }

		  @Override
		  public void ack(Object id) {
			  System.out.println("ack---" + id);
		  }

		  @Override
		  public void fail(final Object id) {
			  System.out.println("fail---" + id);
			  new Thread(){

				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						
					}
					System.out.println("re emit 4.............");
					//一般情况不要直接发，不然会导致不停的fail
					//可以记录到一个统一地方或才重试两次后再做
					_collector.emit(new Values(id), id);
				}
				  
			  }.start();
		  }

		  @Override
		  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declare(new Fields("word"));
		  }

		}
	
  public static class SampleBolt extends BaseBasicBolt {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7748931198303270769L;

	@Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 //declarer.declare(new Fields("gg"));
    }


	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String word = input.getStringByField("word");
		if("4".equals(word)){
			System.out.println("fail----------->" + word);
			throw new FailedException("44444");
		}else{
			//
			//collector.emit(new Values(word));
			System.out.println("success----------->" + word);
		}
		
	}
  }


  public static void main(String[] args) throws Exception {
	  System.setProperty("java.io.tmpdir", "/export/strom/topology/example");
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("s1", new SampleSpout(), 1);
    builder.setBolt("o1", new SampleBolt(), 1).shuffleGrouping("s1");

    Config conf = new Config();
    conf.setDebug(true);


    if (args != null && args.length > 0) {
      conf.setNumWorkers(3);

      StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
    }
    else {
      conf.setMaxTaskParallelism(1);

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("t1", conf, builder.createTopology());
      
      Thread.sleep(60000);

      cluster.shutdown();
    }
  }
}
