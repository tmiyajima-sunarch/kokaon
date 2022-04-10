package jp.co.sunarch.telework.kokaon.controller;

import jp.co.sunarch.telework.kokaon.model.Room;
import jp.co.sunarch.telework.kokaon.model.RoomId;
import jp.co.sunarch.telework.kokaon.usecase.CreateRoomUseCase;
import jp.co.sunarch.telework.kokaon.usecase.ValidateRoomPassCodeUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

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
  private final ValidateRoomPassCodeUseCase validateRoomPassCodeUseCase;

  @PostMapping("/api/v1/room")
  public CreateRoomResultJson createRoom(@RequestBody @Valid CreateRoomInputJson input) {
    Room room = this.createRoomUseCase.execute(input.getName());
    return new CreateRoomResultJson(room.getId().value(), room.getPassCode().value());
  }

  @PostMapping("/api/v1/room/{id}/validate")
  public ValidateRoomResultJson validate(@PathVariable String id, @RequestBody @Valid ValidateRoomInputJson input) {
    try {
      this.validateRoomPassCodeUseCase.execute(new RoomId(id), input.getPasscode());
      return new ValidateRoomResultJson(true);
    } catch (Exception e) {
      return new ValidateRoomResultJson(false);
    }
  }

  @Data
  static class CreateRoomInputJson {
    @NotBlank
    private String name;
  }

  @Value
  static class CreateRoomResultJson {
    String roomId;
    String passcode;
  }

  @Data
  static class ValidateRoomInputJson {
    @NotBlank
    private String passcode;
  }

  @Value
  static class ValidateRoomResultJson {
    boolean ok;
  }
}
