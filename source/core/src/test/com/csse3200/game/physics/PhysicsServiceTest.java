package com.csse3200.game.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class PhysicsServiceTest {
  @Test
  void shouldGetSetEngine() {
    PhysicsEngine engine = mock(PhysicsEngine.class);
    PhysicsService service = new PhysicsService(engine);
    assertEquals(engine, service.getPhysics());
  }
}