package console;

import validate.DataValidator;

import java.util.Scanner;

public class UI {

    private final int QUANTITY_OF_REQUEST_VARIANTS = 6;

    private final Scanner scanner = new Scanner(System.in);

    public Request getConsoleRequest() {
        System.out.println("1 - Create user\n2 - Read all users\n3 - Read user by id\n4 - Update user\n5 - Delete user\n6 - Exit");
        int requestNumber = scanRequestNumber();
        return switch (requestNumber) {
            case 1 -> Request.CREATE;
            case 2 -> Request.READ_ALL;
            case 3 -> Request.READ_BY_ID;
            case 4 -> Request.UPDATE;
            case 5 -> Request.DELETE;
            case 6 -> Request.EXIT;
            default -> throw new IllegalStateException("Unexpected value: " + requestNumber);
        };
    }

    public Long getConsoleId() {
        System.out.println("Id: ");
        Long id;
        while (true){
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                System.out.println("ID cannot be empty");
                continue;
            }
            try {
                id = Long.parseLong(input);
                DataValidator.validateId(id);
                return id;
            } catch (NumberFormatException e) {
                System.out.println("You need to enter an integer");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getConsoleName(boolean canBeBlank) {
        System.out.println("Name: ");
        while (true){
            String name = scanner.nextLine().trim();

            if (name.isBlank()) {
                if (canBeBlank) {
                    return null;
                }
                System.out.println("Name can't be empty");
                continue;
            }
            DataValidator.validateName(name);
            return name;
        }
    }

    public String getConsoleEmail(boolean canBeBlank) {
        System.out.println("Email: ");
        while (true) {
            String email = scanner.nextLine().trim();
            if (email.isBlank()) {
                if (canBeBlank) {
                    return null;
                }
                System.out.println("Email can't be empty");
                continue;
            }
            DataValidator.validateEmail(email);
            return email;
        }
    }

    public Integer getConsoleAge(boolean canBeBlank) {
        System.out.println("Age: ");
        while (true){
            String input = scanner.nextLine().trim();
            try {
                if(input.isBlank()) {
                    if (canBeBlank) {
                        return null;
                    }
                    System.out.println("Age can't be empty");
                    continue;
                }
                Integer age = Integer.parseInt(input);
                DataValidator.validateAge(age);
                return age;
            } catch (NumberFormatException e) {
                System.out.println("You need to enter an integer");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setConsoleResponse(Object response) {
        System.out.println(response.toString());
    }

    private int scanRequestNumber() {
        while (true){
            String input = scanner.nextLine().trim();
            try {
                if(input.isBlank()) {
                    System.out.println("Request number can't be empty");
                    continue;
                }
                int requestNumber = Integer.parseInt(input);
                DataValidator.checkRange(requestNumber, 1, QUANTITY_OF_REQUEST_VARIANTS, "requestNumber");
                return requestNumber;
            } catch (NumberFormatException e) {
                System.out.println("You need to enter an integer");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
