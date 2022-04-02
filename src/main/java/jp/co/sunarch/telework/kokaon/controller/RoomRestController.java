package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author takeshi
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RoomRestController {
  private final CreateRoomUseCase createRoomUseCase;

  @PostMapping("/api/v1/room")
  public CreateRoomResultJson createRoom(@RequestBody @Valid CreateRoomInputJson input) {
    Room room = this.createRoomUseCase.execute(input.getName());
    return new CreateRoomResultJson(room.getId().value());
  }

  @Data
  static class CreateRoomInputJson {
    @NotBlank
    private String name;
  }

  @Value
  static class CreateRoomResultJson {
    String roomId;
  }
}
