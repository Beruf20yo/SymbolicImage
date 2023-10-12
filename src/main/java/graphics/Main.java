package graphics;


import graphics.image.TextGraphicsConverter;
import graphics.image.TextGraphicsConverterImpl;
import graphics.server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new TextGraphicsConverterImpl(); // Создайте тут объект вашего класса конвертера

        GServer server = new GServer(converter);
        server.start();

//         Или то же, но с выводом на экран:
//        String url = "https://semantica.in/wp-content/uploads/2018/01/580b57fcd9996e24bc43c4c4-300x300.png";
//        converter.setMaxHeight(300);
//        converter.setMaxRatio(3);
//        converter.setMaxWidth(400);
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);
    }
}
