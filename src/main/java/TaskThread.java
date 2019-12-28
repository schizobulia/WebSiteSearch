import java.io.*;

public class TaskThread implements Runnable {

    private String path;
    private String str;

    public TaskThread(String path, String str){
        super();
        this.path = path;
        this.str = str;
    }

    @Override
    public void run() {
        this.writeFileLine(this.path, this.str);
    }

    /**
     * 给文件最后一行写入数据
     * @param path
     * @param str
     * @return
     * @throws IOException
     */
    public boolean writeFileLine(String path, String str) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        boolean tag = true;
        try {
            fos = new FileOutputStream(new File(path), true);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(str);
        } catch (IOException e) {
            tag = false;
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                osw.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tag;
    }

}
