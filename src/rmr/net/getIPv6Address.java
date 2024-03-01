package rmr.net;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;

import rmr.fs.ReadConfig;

import static java.lang.Thread.sleep;

public class getIPv6Address {
    public static void main(String[] args) throws IOException {
        //读取配置
        Properties properties = ReadConfig.getConfig();

        String timeTemp1 = properties.getProperty("getIPv6Interval");
        String timeTemp2 = properties.getProperty("sendMailInterval");
        long time1 = Long.parseLong(timeTemp1);
        long time2 = Long.parseLong(timeTemp2);

        String memberNum = properties.getProperty("memberNum");
        String[] members = new String[Integer.parseInt(memberNum)];
        for(int i = 0; i < members.length; i++){
            StringBuilder member = new StringBuilder();
            members[i] = properties.getProperty(member.append("member").append(i).toString());
        }

        //获取可用ipv6地址
        try {
            String rightNowIPv6Address = getLocalIPv6Address();
            System.out.println(getCrurentTime(Calendar.getInstance()) + " 当前获取到IPv6地址为：【" + rightNowIPv6Address + "】");
            sendMailToUsers(rightNowIPv6Address, members, time2);
            while(true){
                sleep(time1*1000);
                if(!Objects.equals(rightNowIPv6Address, getLocalIPv6Address())){
                    System.out.println(getCrurentTime(Calendar.getInstance()) + " 检测到IPv6地址发生变化！");
                    System.out.println(getCrurentTime(Calendar.getInstance()) + " 当前IPv6地址为：【" + rightNowIPv6Address + "】");
                    System.out.println(getCrurentTime(Calendar.getInstance()) + " 准备发送邮件！");

                    //发送消息
                    rightNowIPv6Address = getLocalIPv6Address();
                    sendMailToUsers(rightNowIPv6Address, members, time2);
                }
            }
        } catch (SocketException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //发送邮件
    public static void sendMailToUsers(String rightNowIPv6Address, String[] members, long time2) throws InterruptedException {
        for (String s : members) {
            sleep(time2 * 1000);
            Email.sendMassage(s, rightNowIPv6Address);
            System.out.println(getCrurentTime(Calendar.getInstance()) + " 向【" + s + "】发送邮件成功！");
        }
    }

    //获取本地ipv6地址
    public static String getLocalIPv6Address() throws SocketException {
        InetAddress inetAddress =null;
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        outer:
        while(networkInterfaces.hasMoreElements()) {
            Enumeration<?> inetAds = networkInterfaces.nextElement().getInetAddresses();
            while(inetAds.hasMoreElements()) {
                inetAddress = (InetAddress)inetAds.nextElement();

                //检查此地址是否是IPv6地址以及是否是保留地址
                if(inetAddress instanceof Inet6Address&& !isReservedAddr(inetAddress)) {
                    break outer;
                }
            }
        }

        assert inetAddress != null;
        String ipAddr = inetAddress.getHostAddress();

        //过滤网卡
        int index = ipAddr.indexOf('%');
        if(index>0) {
            ipAddr = ipAddr.substring(0, index);
        }
        return ipAddr;
    }

    //是否是无效地址
    private static boolean isReservedAddr(InetAddress inetAddr) {
        /*
        AnyLocalAddress：通配符地址，即全0地址
        LinkLocalAddress：本地连接地址
        LoopbackAddress：回环地址
         */
        return inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress();
    }

    //获取当前时间
    private static String getCrurentTime(Calendar calendar){
        int month = calendar.get(Calendar.MONTH) + 1;
        return calendar.get(Calendar.YEAR) + "-" + month + "-" + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }
}
