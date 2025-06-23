package com.groupx.gui;

import com.groupx.gui.controller.GuiController;
import com.groupx.gui.service.RestClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class GuiControllerTest {

    // Injected by TestConfig
    private final RestClient mockClient = Mockito.mock(RestClient.class);

    @BeforeEach
    void stubRestClient() {
        // wire up whatever responses your tests need:
        // e.g. when(mockClient.getCurrentPercentage()).thenReturn(new CurrentPercentageFx(...));
    }

    @Start
    private void start(Stage stage) throws Exception {
        // load your FXML and inject the TestConfig beans into the controller
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == RestClient.class) return mockClient;
            try { return clazz.getDeclaredConstructor().newInstance(); }
            catch (Exception e) { throw new RuntimeException(e); }
        });
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testRefreshUpdatesLabels(FxRobot robot) {
        // arrange: stub current‐data
        // e.g. when(mockClient.getCurrentPercentage())
        //          .thenReturn(new CurrentPercentageFx(80.0, 16.67));
        // act:
        robot.clickOn("#refreshButton");
        // assert:
        Label comm = robot.lookup("#communityDepletedLabel")
                .queryAs(Label.class);
        Label grid = robot.lookup("#gridPortionLabel")
                .queryAs(Label.class);
        assertThat(comm).hasText("80.00");
        assertThat(grid).hasText("16.67");
    }

    @Test
    void testLoadHistoryPopulatesTable(FxRobot robot) {
        // arrange: stub history data...
        // act:
        robot.clickOn("#loadHistoryButton");
        // assert:
        @SuppressWarnings("unchecked")
        TableView<?> table = robot.lookup("#historyTable")
                .queryAs(TableView.class);
        assertThat(table.getItems()).hasSize(2);
    }
}
