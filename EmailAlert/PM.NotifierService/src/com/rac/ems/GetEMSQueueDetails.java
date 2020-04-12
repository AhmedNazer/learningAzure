package com.rac.ems;
import com.tibco.tibjms.admin.*;
import com.tibco.security.*;


public class GetEMSQueueDetails {
	
/*	public static void main(String args[]) throws TibjmsAdminException
	{
	String jmsUrl = "tcp://localhost:7222";
	String jmsUserName="admin";
	String jmsPassword="";
	//GetDetails(jmsUrl, jmsUserName, jmsPassword);
	
	System.out.println(GetDetails(jmsUrl, jmsUserName, jmsPassword));
	}
*/	public static String DecryptPwd(String EncryptedPassword) throws AXSecurityException
	{
		String jmsPassword = new String(ObfuscationEngine.decrypt(EncryptedPassword)); 		
		return jmsPassword;
	}
	public static String GetDetails(String jmsUrl,String jmsUserName,String jmsPassword) throws TibjmsAdminException, AXSecurityException
	{
		//String jmsPassword = DecryptPwd(EncryptedPassword);		
        String jsFormatA = "\"%s\": \"" + "%s" + " \", ";
        String jsFormatB = "\"%s\": \"" + "%s" + " \"";
        StringBuilder sb = new StringBuilder();
               
		
        // Connection to EMS via Administration API
        TibjmsAdmin jmsadmin = new TibjmsAdmin(jmsUrl, jmsUserName, jmsPassword);
        
        
        try {
            /******************************************************
             * Define Server Info
             ******************************************************/
            
        	sb.append("{");
            sb.append("\"ems_monitor\":");
            sb.append("{");
            StateInfo stateInfo = jmsadmin.getStateInfo();
            sb.append(String.format(jsFormatA, "serverName",
                    stateInfo.getServerName()));
            if (stateInfo.getState().get() == State.SERVER_STATE_ACTIVE_STANDALONE) {
                sb.append(String.format(jsFormatA, "state", "Standalone"));
            } else if (stateInfo.getState().get() == State.SERVER_STATE_STANDBY) {
                sb.append(String.format(jsFormatA, "state", "Standby"));
            } else if (stateInfo.getState().get() == State.SERVER_STATE_ACTIVE) {
                sb.append(String.format(jsFormatA, "state", "Active"));
            } else {
                sb.append(String.format(jsFormatA, "state", stateInfo
                        .getState().get()));
            }
            /******************************************************
             * Define the queues part
             ******************************************************/
            sb.append("\"queues\":");
            sb.append("{");
            QueueInfo[] queueInfos = jmsadmin.getQueues();
            if (queueInfos.length > 0) {
                sb.append("\"queue\":[");
                for (int i = 0; i <= queueInfos.length - 1; i++) {
                    if (i >= 1)
                        sb.append(",");
                    sb.append("{");
                    sb.append(String.format(jsFormatA, "queueName",queueInfos[i].getName()));
                    sb.append(String.format(jsFormatA, "nbMessageCount",
                            queueInfos[i].getPendingMessageCount()));
                    sb.append(String.format(jsFormatA,
                            "inboundStatGetTotalMessages", queueInfos[i]
                                    .getInboundStatistics().getTotalMessages()));
                    sb.append(String
                            .format(jsFormatA, "outboundStatGetTotalMessages",
                                    queueInfos[i].getOutboundStatistics()
                                            .getTotalMessages()));
                    sb.append(String.format(jsFormatB, "maxMessagesSupported",
                            queueInfos[i].getMaxMsgs()));
                    sb.append("}");
                    // System.out.println(String.format("queue name %s, size %s, consumerCount %s, maxMessages Supported %s",queueInfos[i].getName(),queueInfos[i].getPendingMessageCount(),
                    // queueInfos[i].getConsumerCount(),queueInfos[i].getMaxMsgs()));
                }
                sb.append("]");
            } else {
                sb.append("\"queue\":[");
                /*
                 * sb.append("{"); sb.append(String.format(jsFormatA,
                 * "queueName", "")); sb.append(String.format(jsFormatA,
                 * "nbMessageCount", 0)); sb.append(String.format(jsFormatB,
                 * "maxMessagesSupported", 0)); sb.append("}");
                 */
                sb.append("]");
            }
            sb.append("}");
            sb.append("}");
            sb.append("}");
                
}
        finally {
			jmsadmin.close();
			jmsadmin=null;
			//sb = null;
			}
return sb.toString();
}}

