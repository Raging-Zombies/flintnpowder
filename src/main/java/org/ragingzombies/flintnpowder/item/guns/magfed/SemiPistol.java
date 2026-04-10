package org.ragingzombies.flintnpowder.item.guns.magfed;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.ragingzombies.flintnpowder.core.guns.FlintlockBase;
import org.ragingzombies.flintnpowder.core.guns.MagfedBase;
import org.ragingzombies.flintnpowder.item.ammo.CastIronRoundshot;
import org.ragingzombies.flintnpowder.item.ammo.CopperRoundshot;
import org.ragingzombies.flintnpowder.item.ammo.magazines.HandgunMag;
import org.ragingzombies.flintnpowder.sound.ModSounds;

import javax.annotation.Nullable;
import java.util.List;

public class SemiPistol extends MagfedBase {
    public SemiPistol(Properties pProperties) {
        super(pProperties);
        shootCooldownTicks = 1;
    }

    @Override
    public boolean checkMagazine(ItemStack mag) {
        if (mag.getItem() instanceof HandgunMag) return true;

        return false;
    }

    @Override
    public float accuracyModifier() {
        return 1;
    }

    @Override
    public void onShoot(Level pLevel, LivingEntity shooter, ItemStack gunStack) {
        pLevel.playSeededSound(null, shooter.getBlockX(), shooter.getBlockY(), shooter.getBlockZ(),
                ModSounds.PISTOLSHOOT.get(), SoundSource.NEUTRAL, 3.0F, 1.0F, 0);
        pLevel.playSeededSound(null, shooter.getBlockX(), shooter.getBlockY(), shooter.getBlockZ(),
                ModSounds.PISTOLDISTANTSHOOT.get(), SoundSource.NEUTRAL, 9.0F, 1.0F, 0);

        // Particles
        if (!pLevel.isClientSide()) {
            ServerLevel sLevel = (ServerLevel) pLevel;
            for (int index0 = 0; index0 < 3; index0++) {
                double speed = 0.15;
                double spread = 0.05;

                sLevel.sendParticles(
                        ParticleTypes.POOF,
                        shooter.getX(), shooter.getY() + shooter.getEyeHeight() * 0.6, shooter.getZ(),
                        0,
                        shooter.getDeltaMovement().x + shooter.getLookAngle().x * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread),
                        shooter.getDeltaMovement().y + shooter.getLookAngle().y * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread),
                        shooter.getDeltaMovement().z + shooter.getLookAngle().z * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread),
                        1.0
                );
            }
        }

        if (shooter instanceof Player) {
            ((Player) shooter).getCooldowns().addCooldown(this, shootCooldown(shooter, gunStack));
        }
    }
}
