package com.minerforstone.conductive.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RedstoneComponent extends Block {

    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public RedstoneComponent(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(OPEN, true));
    }

    protected Identifier LID_ID = new Identifier("minecraft:smooth_stone_slab");

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (tryOpen(player, world, pos, state)) return ActionResult.SUCCESS;
        if (tryClose(player,world, pos, state)) return ActionResult.SUCCESS;

        if (world.isClient) player.sendMessage(Text.literal("You touched me at: " + getTouchedPixel(hit).x + ", " + getTouchedPixel(hit).y));

        return ActionResult.SUCCESS;
    }

    protected boolean tryClose(PlayerEntity player, World world, BlockPos pos, BlockState state) {
        if (player.getMainHandStack().getItem().equals(Registries.ITEM.get(LID_ID)) && world.getBlockState(pos).get(OPEN)) {
            world.setBlockState(pos, state.with(OPEN, false));
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 10, 1.5f);
            if (!player.isCreative()) player.getMainHandStack().decrement(1);
            return true;
        } else return false;
    }

    protected boolean tryOpen(PlayerEntity player, World world, BlockPos pos, BlockState state) {
        if (player.getMainHandStack().isEmpty() && !world.getBlockState(pos).get(OPEN)) {
            world.setBlockState(pos, state.with(OPEN, true));
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 10, 1.5f);
            if (!player.isCreative()) player.giveItemStack(Registries.ITEM.get(LID_ID).getDefaultStack());
            return true;
        } else return false;
    }

    protected Vec2f getTouchedPixel(BlockHitResult hit) {
        Vec3d hitPos = hit.getPos();
        int x = Math.floorMod((int) Math.round(16 * hitPos.x - 0.5), 16);
        int y = Math.floorMod((int) Math.round(16 * hitPos.z - 0.5), 16);

        return new Vec2f(x, y);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.0625f, 1f);
        else return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.125f, 1f);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }
}
