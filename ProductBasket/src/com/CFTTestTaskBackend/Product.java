package com.CFTTestTaskBackend;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

public class Product {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private StringBuilder builder = new StringBuilder();

    // поля заказа - название, цена, количество, стоимость
    private String name;
    private double price;
    private int value;
    private double sum;

    Product() {
    }

    Product(String name, double price, int value, double sum) {
        this.name = name;
        this.price = price;
        this.value = value;
        this.sum = sum;
    }

    private String[] productList = new String[15]; // массив для считывания названия продуктов из справочника товаров
    private ArrayList<Product> products = new ArrayList<>(productList.length); // массив для создания массива объектов существующих товаров
    ArrayList<Product> userProducts = new ArrayList<>(); // массив для размещения выбранных пользователем товаров
    private int orderID = (int) (Math.random() * 100000); // номер заказа

    private String userInfo; // строка с данными о номере заказа, пользователе, дате
    private String headInfo; // строка шапки таблицы заказов
    private String productsInfo = ""; // строка с данными о продуктах в корзине пользователя
    private String totalInfo; // строка с итоговой суммой заказа

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод считывает названия товаров из файла ProductsList.txt и записывает их в массив productList.
     * */

    public void readingFile() throws FileNotFoundException {

        String productsListFile = "ProductsList.txt";

        FileInputStream fis = new FileInputStream(productsListFile);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
            String s;
            int i = 0;

            while ((s = reader.readLine()) != null) {
                productList[i] = s;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // readFile()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод округляет переданные в него значения до 1 знака после запятой.
     * */

    public double round(double d) {
        d = Math.round(d * 10) / 10.0;
        return d;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод заполняет массив объектов продуктами. Названия берутся из productList и в цикле заполняется цена, количество и сумма товаров.
     * */

    public void createProducts() {
        double i = 0;
        int j = 0;

        for (String s : productList) {
            products.add(new Product(s, i += 12.46, j, i * j));
        }
        for (Product p : products) {
            p.price = round(p.price);
            p.sum = round(p.sum);
        }
    } // createProducts()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод обрабатывает пользовательский ввод в случае, если пользователь хочет сделать заказ, после чего объекты заказа помещаются в userProducts.
     * */

    public void takeUserInput() throws IOException {

        boolean flag; // флаговая промежуточная переменная, указывает на наличие элементов в userProducts

        String userInput = " "; // пользователь вводит название товара и число, разделённые пробелом
        String[] productValue; // промежуточный массив на хранение разделённой строки userInput, где хранится название товара и его количество как два отдельных элемента
        String input; // разделённое название товара от userInput
        int inputValue; // разделённое количество товара от userInput

        System.out.println("Вы можете вводить строки с наименованием товаров и их количеством через клавишу Enter. После окончания ввода нажмите клавишу Enter ещё раз для заполнения корзины товаров.");
        System.out.println("Введите наименование товара и его количество через пробел (пример: <товар> <количество>) :");

        while (!userInput.isEmpty()) { // проверка на пустой пользовательский ввод
            while (true) {
                flag = true;
                userInput = reader.readLine();
                if (userInput.equals(""))
                    break;
                productValue = userInput.split(" "); // заполнение промежуточного массива названием товара и его количеством

                // цикл берёт объект товара из products и сравнивает его с пользовательским вводом
                for (int i = 0; i < products.size(); i++) {
                    Product p = products.get(i);
                    String nameProduct = p.name; // название продукта из products
                    input = productValue[0];
                    inputValue = Integer.parseInt(productValue[1]);

                    // проверка на наличие элементов в userProducts. Если пользователь введёт товар, который есть в userProducts, то добавление этого товара как нового элемента не произойдёт,
                    // но его количество и сумма увеличатся. flag меняет своё значение
                    if (userProducts.size() != 0) {
                        for (Product pp : userProducts) {
                            String up = pp.name;
                            if (input.equalsIgnoreCase(up)) {
                                pp.value += inputValue;
                                pp.sum += inputValue * p.price;
                                pp.sum = round(pp.sum);
                                flag = false;
                                break;
                            }
                        }
                    }

                    // если userProducts пустой, то проверяется совпадение пользовательского ввода и имени товара из products
                    if (flag && input.equalsIgnoreCase(nameProduct)) {
                        p.value += inputValue;
                        p.sum = p.price * p.value;
                        p.sum = round(p.sum);
                        userProducts.add(p);
                        break;
                    }
                    // если достигнут последний элемент products без попадания в предыдущие проверки, то выдаётся ошибка ввода
                    else if (nameProduct.equals(products.get(products.size() - 1).name))
                        System.out.println("Введите корректное наименование товара.");
                        // если было совпадение в первой проверке, производится выход из цикла
                    else if (!flag)
                        break;
                }
                break;
            }
        } // while (!userInput.isEmpty())
    } // takeUserInput()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для печати списка продуктов из справочника, который представляет собой массив productsList.
     * */

    public void printList() {
        for (String s : productList) {
            System.out.println(s);
        }
    } // printList()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для печати полей заказа, шапки таблицы, заказов и итоговой суммы заказа.
     * */

    public void printOrder() {
        String user = "Иванов Иван Иванович";
        Calendar calendar = Calendar.getInstance();

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        int day = calendar.get(Calendar.DATE);
        String month = months[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);

        String date = day + "." + month + "." + year;

        userInfo = "Заказ №" + orderID + " " + user + " " + date + "\n\n";

        System.out.print(userInfo);
        printHead();
        printBasket();
        printTotal();
    } // printOrder()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для печати шапки таблицы товаров с определёнными отступами между столбцами.
     * */

    public void printHead() {
        String space = " ";

        String name = "Название";
        String nameLength = space.repeat(20 - name.length());

        String price = "Цена";
        String priceLength = space.repeat(20 - price.length());

        String value = "Количество";
        String valueLength = space.repeat(15 - value.length());

        String sum = "Сумма";
        String sumLength = space.repeat(20 - sum.length());

        headInfo = name + nameLength + price + priceLength + value + valueLength + sum + sumLength + "\n";

        System.out.println(headInfo);
    } // printHead()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для печати переданного продукта с определёнными отступами между столбцами аналогично шапке.
     * */

    public void printProduct(Product p) {
        String space = " ";

        String productLength = space.repeat(20 - p.name.length());

        String priceLength = String.valueOf(p.price);
        priceLength = space.repeat(20 - priceLength.length());

        String valueLength = Integer.toString(p.value);
        valueLength = space.repeat(15 - valueLength.length());

        String sumLength = String.valueOf(p.sum);
        sumLength = space.repeat(20 - sumLength.length());

        productsInfo += p.name + productLength + p.price + priceLength + p.value + valueLength + p.sum + sumLength + "\n";
    } // printProduct()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для сортировки всех продуктов внутри userProducts по алфавиту и печати всех продуктов,
     * находящихся в userProducts, в цикле по методу printProduct() с элементом userProducts в качестве параметра.
     * */

    public void printBasket() {
        productsInfo = "";
        Product firstSwap;
        Product secondSwap;

        // сортировка происходит пузырьковым методом с попарной заменой элементов userProducts
        for (int i = userProducts.size() - 1 ; i > 0 ; i--) {
            for (int j = 0 ; j < i ; j++) {
                if (userProducts.get(j).price > userProducts.get(j + 1).price) {
                    firstSwap = userProducts.get(j);
                    secondSwap = userProducts.get(j + 1);
                    userProducts.set(userProducts.indexOf(userProducts.get(j + 1)), firstSwap);
                    userProducts.set(userProducts.indexOf(userProducts.get(j)), secondSwap);
                }
            }
        }

        for (Product p : userProducts) {
            printProduct(p);
        }
        System.out.print(productsInfo);
    } // printBasket()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для печати итоговой суммы сформированного заказа.
     * */

    public void printTotal() {
        double total = 0;

        for (Product p : userProducts) {
            total += p.sum;
            total = round(total);
        }

        totalInfo = "\nИтого: " + total;
        System.out.println(totalInfo);
    } // printTotal()

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для заполнения StringBuilder информацией о заказе, шапке таблицы, всех продуктах и итоговой сумме сформированного заказа.
     * */

    public void fillBuilder() {
        if (productsInfo.isEmpty())
            builder.append("Заказ не сформирован, ничего не заказано. Вы можете вернуться в программу и произвести оформление заказа.");
        else
            builder.append(userInfo).append(headInfo).append(productsInfo).append(totalInfo).append("\n");
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Метод для записи сформированного заказа со всей информацией в файл userOrder.txt.
     * */

    public void writingFile() {

        String fileName = "userOrder.txt";

        if (builder.length() == 0) {
            fillBuilder();
        }

        try(FileWriter writer = new FileWriter(fileName)) {
            writer.write(String.valueOf(builder));
            writer.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    } // writingFile()

} // Product
