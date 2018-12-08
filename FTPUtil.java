import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-12-02 19:09
 **/
public class FTPUtil {
    private static final Logger logger =LoggerFactory.getLogger(FTPUtil.class);


    //服务器ip
    private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    //ftp用户
    private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    //ftp密码
    private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 将文件上传到ftp服务器上
     * @param fileList
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接ftp服务器");
        //上传到ftp服务器根目录下的img文件夹下
        boolean result=ftpUtil.uploadFile("img",fileList);
        logger.info("结束上传，上传结果:{}");
        return result;
    }

    /**
     *
     * @param remotePath 上传到ftp服务器的路径
     * @param fileList 需要上传的文件列表
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean upload=true;
        FileInputStream fis=null;
        //连接ftp服务器
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                ftpClient.makeDirectory(remotePath);
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开本地的被动模式
                for(File fileItem:fileList){
                    fis=new FileInputStream(fileItem);

                    //FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码。
                    String filename=new String(fileItem.getName().getBytes("UTF-8"),"iso-8859-1");
                    ftpClient.storeFile(filename,fis);
                }
            } catch (IOException e) {
                logger.error("上传文件 异常",e);
                upload=false;
            }
            finally {
                fis.close();
                ftpClient.disconnect();
            }
        }else {
            logger.info("连接服务器失败，请检查服务器地址、账号、密码");
            return false;
        }
        return upload;
    }

    /**
     * 连接ftp服务器
     * @param ip 服务器ip
     * @param port 服务器端口
     * @param user 用户名
     * @param pwd 密码
     * @return
     */
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess=false;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess=ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接ftp服务器异常",e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static void main(String[] args) throws IOException {
        List<File> fileList=new ArrayList<>();
        File file1=new File("/Users/xiaowen/Downloads/最强.jpg");
        File file2=new File("/Users/xiaowen/Downloads/妖精的女王.jpg");
        fileList.add(file1);
        fileList.add(file2);
        FTPUtil.uploadFile(fileList);
    }
}
