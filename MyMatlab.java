package com.face.utils.matlab;

import com.face.common.Const;
import com.face.utils.imagedetail.ImageUtils;
import matlabcontrol.*;

import java.io.File;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-12-26 08:16
 **/


/**
 * matlab中的测试方法
 * 
 * function[c]=sumTest(a,b)
 * c=a+b;
 * end
 */
public class MyMatlab {
    private static MatlabProxy proxy = null;

    //连接matlab
    public static void connectMatlab() {
        File file = new File(Const.MatlabRootPath);
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setProxyTimeout(300000L)//java调用matlab的超时时间，默认180000 ms
                .setMatlabStartingDirectory(file)//设置MATLAB的开始目录。
                .setHidden(false).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        try {
            //获取matlab实例
            proxy = factory.getProxy();
            System.out.println("matlab连接成功");
        } catch (MatlabConnectionException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 断开matlab连接
     */
    public static void disconnectMatlab() {
        if (proxy.isConnected()) {
            proxy.disconnect();
        }

    }

    public static double[] sumTest(int a, int b) throws MatlabInvocationException {
        Object[] result = proxy.returningFeval("sumTest", 1, a, b);
        System.out.println(result);
        return (double[]) result[0];
    }


    /**
     * @param img1path
     * @param img2path
     * @param savaPath  光流文件保存路径
     * @param imageName 光流文件名称
     * @throws MatlabInvocationException
     */
    public static synchronized void mylightflow(String img1path, String img2path, String savaPath, String imageName) throws MatlabInvocationException {

        System.out.println();
        img2path = ImageUtils.resizeImage(img1path, img2path);

        proxy.returningFeval("mylightflow", 0, img1path, img2path, savaPath, imageName);
        if (img2path.contains("resize")) {
            File file = new File(img2path);
            file.delete();
        }
    }

    public static void main(String[] args) throws Exception {
        connectMatlab();
        try {
            System.out.println(sumTest(2, 3));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnectMatlab();
        }
    }
}
