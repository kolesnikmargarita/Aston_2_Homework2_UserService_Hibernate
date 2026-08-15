package console;

import controller.UserController;
import dto.*;
import exception.*;

import java.util.Scanner;

public class Console {

    private final UserController userController;
    private final UI ui = new UI();
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    public Console(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        while (true) {
            try{
                Request request = ui.getConsoleRequest();
                if (request == Request.EXIT) {
                    break;
                } else {
                    setRequest(request);
                }
            } catch (UserNotFoundException e) {
                System.out.println (handler.handleUserNotFoundException(e));
            } catch (EmptyDataException e) {
                System.out.println(handler.handleEmptyDataException(e));
            } catch (NotCorrespondingTypeException e) {
                System.out.println(handler.handleNotCorrespondingTypeException(e));
            } catch (OutOfRangeException e) {
                System.out.println(handler.handleOutOfRangeException(e));
            } catch (DatabaseException e) {
                System.out.println(handler.handleDatabaseException(e));
            } catch (Exception e) {
                System.out.println(handler.handleGenericException(e));
            }
        }
    }

    private void setRequest(Request request) {
        switch (request) {
            case Request.CREATE -> {
                CreateUserDto dto = new CreateUserDto(
                        ui.getConsoleName(false),
                        ui.getConsoleEmail(false),
                        ui.getConsoleAge(false));

                ui.setConsoleResponse(userController.create(dto));
            }
            case READ_ALL -> {
                ui.setConsoleResponse(userController.readAll());
            }
            case READ_BY_ID -> {
                ReadUserByIdDto dto = new ReadUserByIdDto(ui.getConsoleId());
                ui.setConsoleResponse(userController.readById(dto));
            }
            case UPDATE -> {
                UpdateUserDto dto = new UpdateUserDto(
                        ui.getConsoleId(),
                        ui.getConsoleName(true),
                        ui.getConsoleEmail(true),
                        ui.getConsoleAge(true)
                );
                ui.setConsoleResponse(userController.update(dto));
            }
            case DELETE -> {
                DeleteUserDto dto = new DeleteUserDto(ui.getConsoleId());
                userController.delete(dto);
            }
            case null -> {
                throw new EmptyDataException("Enter request number");
            }
            default -> {
                throw new OutOfRangeException("There isn't this request");
            }
        }
    }
}
