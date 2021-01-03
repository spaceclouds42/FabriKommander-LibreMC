package me.gserv.fabrikommander.mixin;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TranslatableText.class)
public abstract class TranslatableTextMixin {
    protected TranslatableTextMixin(String key, Object[] args) {
        this.key = key;
        this.args = args;
    }

    @Inject(method = "<init>(Ljava/lang/String;[Ljava/lang/Object;)V", at = @At("RETURN"))
    public void TranslatableText(String key, Object[] args, CallbackInfo ci) {
        System.out.println("doing fancy translatable text stuffs");
        this.key = key;
        this.args = args;

        if (key.startsWith("chat.type.text")) {
            for (int i = 0; i < args.length; ++i) {
                Object object = args[i];
                if (object instanceof String) {
                    this.args[i] = new LiteralText((String) object);
                } else if (object instanceof Text) {
                    Text text = ((Text) object).shallowCopy(); //see if copy or deepcopy are better
                    this.args[i] = text;
                } else if (object == null) {
                    this.args[i] = "null";
                }
            }
        }
        System.out.println("done with translatable text stuffs");
    }

    @Mutable
    @Final
    @Shadow
    private String key;
    @Mutable
    @Final
    @Shadow
    private Object[] args;
}
