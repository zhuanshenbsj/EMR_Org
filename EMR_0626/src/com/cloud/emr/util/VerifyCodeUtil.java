package com.cloud.emr.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * 
 * 项目名称：EMR   
 * 类名称：VerifyCodeUtil   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-26 上午10:03:42   
 * 修改人：lw   
 * 修改时间：2014-2-26 上午10:03:42   
 * 修改备注： 
 * @version
 */
public class VerifyCodeUtil {
    private static int width = 220;         
    private static int height = 30;         
    private static int codeCount = 4;         
    private static int x = width / (codeCount+1);         
    private static int fontHeight = height-2;         
    private static int codeY = height-4;         
    private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',         
    	'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',         
    	'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    public static void getVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);    
        Graphics2D g = buffImg.createGraphics();         
        Random random = new Random();         
        g.setColor(Color.WHITE);         
        g.fillRect(0, 0, width, height);         
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);         
        g.setFont(font);         
        g.setColor(Color.BLACK);         
        g.drawRect(0, 0, width - 1, height - 1);         
        g.setColor(Color.BLACK);         
        for (int i = 0; i < 10; i++) {         
            int x = random.nextInt(width);         
            int y = random.nextInt(height);         
            int xl = random.nextInt(12);         
            int yl = random.nextInt(12);         
            g.drawLine(x, y, x + xl, y + yl);         
        }         
        StringBuffer randomCode = new StringBuffer();         
        int red = 0, green = 0, blue = 0;         
        for (int i = 0; i < codeCount; i++) {         
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);      
            red = random.nextInt(255);         
            green = random.nextInt(255);         
            blue = random.nextInt(255);         
            g.setColor(new Color(red, green, blue));   
            if(i==0)
            	g.drawString(strRand, x, codeY);         
            else
            	g.drawString(strRand, (i + 1) * x, codeY);         
            randomCode.append(strRand);         
        } 
        HttpSession session = req.getSession();         
        session.setAttribute("validateCode", randomCode.toString());         
        System.out.println(randomCode.toString());
        resp.setHeader("Pragma", "no-cache");         
        resp.setHeader("Cache-Control", "no-cache");         
        resp.setDateHeader("Expires", 0);         
        resp.setContentType("image/jpeg");         
        ServletOutputStream sos = resp.getOutputStream();         
        ImageIO.write(buffImg, "jpeg", sos);         
        sos.close();         
    }         
}
