package twofive.ncmovement.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Unique
    private boolean lastForward, lastSideways;

    @Inject(method = "tick", at = @At("TAIL"))
    private void postTick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        boolean f = pressingForward;
        boolean b = pressingBack;
        boolean l = pressingLeft;
        boolean r = pressingRight;

        if (f != b) lastForward = f;
        if (l != r) lastSideways = l;

        if (f && b) { movementForward = resolveConflict(lastForward, slowDown, slowDownFactor); }
        if (l && r) { movementSideways = resolveConflict(lastSideways, slowDown, slowDownFactor); }
    }

    @Unique
    private float resolveConflict(boolean lastPositive, boolean slowDown, float slowDownFactor) {
        float value = lastPositive ? -1.0f : 1.0f;
        return slowDown ? value * slowDownFactor : value;
    }
}