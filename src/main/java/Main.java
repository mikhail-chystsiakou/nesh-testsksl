import inheritance.Shape;


public class Main {
    public static void main(String[] args) {
        System.out.println(
            "u=82-234-81-25; s=yt%3A1-sc%3A0-vk%3A1-yd%3A0-4s%3A0-ar%3A0"
                .replaceAll(
                    "^(.*s=[^;]*4s%3A)(\\d)(-.*)$",
                    "$11$3"
                )
            );
        System.out.println("Ok go");

//        Shape a = new Shape();
//        Shape b = new Shape();
//
//        a.width = 3;
//        b.height = 4;
//        a.height = 5;
//        b.width = a.calculateArea();
//        System.out.println(b.calculateArea());
    }
}