#encoding: utf-8
import sys
import os
import time

'''
nohup python clear.py > out.log &
'''


def clear(dirname):
    print 'process ', dirname
    for childname in os.listdir(dirname):
        catalina_file = os.path.join(dirname, childname, 'logs/catalina.out')
        if os.path.exists(catalina_file):
           size = os.path.getsize(catalina_file)
           print catalina_file + "==>" + str(size)
           if size > 1024000000:
               print catalina_file + "==>" + str(size) + " clear!!!"
               f=open(catalina_file,'w')
               f.truncate()
               f.close()

def clearAppLog(dirname):
    print 'process ', dirname
    for childname in os.listdir(dirname):
        if childname.endswith('.com'):
            rmReg = dirname + "/" + childname + "/*.log.20*"
            print rmReg
            os.system('rm ' + rmReg)
                      
def hello():
    print "hello"


COMMANDS = {"hello": hello}

if __name__ == '__main__':
    (COMMANDS.get("hello"))()
    (globals()['hello'])()
    dirname = '/export/home/tomcat/domains'
    dirname = 'E:/export/home/tomcat'
    while True:
        try:
            clear(dirname)
            clearAppLog('E:/export/home/tomcat/logs')
        except Exception, Error:
            print 'error'
        finally:
            print 'over'
                
        print '===============sleep 60 * 60s========================'
        time.sleep(60)
        

