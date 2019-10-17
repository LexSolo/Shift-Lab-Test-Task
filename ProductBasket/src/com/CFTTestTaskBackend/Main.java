package com.CFTTestTaskBackend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    // переменная для проверки окончания программы и выход из неё
    private static boolean flag = true;

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // переменные для взаимодействия с пользователем и обработки введённой информации для дальнейшего хода программы
        int answer;
        int answer2;

        Product product = new Product();

        // вызов метода для чтения информации о доступных товарах из файла и записи их в массив внутри программы
        try {
            product.readingFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        product.createProducts();

        // вывод главного меню и его подпунктов программы и проверка на продолжение выполнения программы
        while (flag) {
            System.out.println("\nВыберите действие:\n1 - сделать заказ;\n2 - проверить состояние корзины;\n3 - просмотреть справочник товаров;\n0 - выход из программы.\n");

            // ввод информации о дальнейших действиях (совершении заказа, проверки состояния корзины и др.) и проверка на правильность ввода
            while (true) {
                try {
                    answer = Integer.parseInt(reader.readLine());
                    System.out.println();
                    break;
                } catch (Exception e) {
                    System.out.println("Допустим ввод только чисел.");
                }
            }

            // обработка введённой информации
            switch (answer) {
                // процедура совершения заказа и вызов соответствующего метода
                case 1:
                    try {
                        product.takeUserInput();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                // печать корзины в консоль и вызов соответствующих методов
                case 2:
                    // проверка на наличие товаров в корзине
                    if (product.userProducts.isEmpty()) {
                        System.out.println("В корзине пока нет товаров, заказ не сформирован. Хотите выбрать товары (1 - да, 0 - нет) ?");

                        // обработка ввода пользователя
                        while (true) {
                            try {
                                answer2 = Integer.parseInt(reader.readLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Допустим ввод только чисел.");
                            }
                        }

                        // программа идёт на оформление заказа, либо выводит пользователя в главное меню
                        switch (answer2) {
                            case 1:
                                try {
                                    product.takeUserInput();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 0:
                                break;
                        }
                    }
                    // в случае наличия товаров в корзине пользователя идёт печать заказа в консоль
                    else
                        product.printOrder();
                    break;
                // печать в консоль списка товаров из строкового массива, ранее полученного из файла
                case 3:
                    product.printList();
                    break;
                // осуществление записи сформированного заказа в файл, завершение программы
                case 0:
                    product.writingFile();
                    System.out.println("Все данные по вашему заказу записаны в файл \"userOrder.txt\", который находится в папке с данным проектом.");
                    System.out.println("Спасибо за посещение, до скорых встреч!");
                    flag = false;
                    break;
            } // switch (answer)
        } // while (flag)
    } // main()
} // Main
