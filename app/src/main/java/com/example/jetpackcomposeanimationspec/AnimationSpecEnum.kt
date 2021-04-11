package com.example.jetpackcomposeanimationspec

import androidx.compose.animation.SplineBasedFloatDecayAnimationSpec
import androidx.compose.animation.core.FloatDecayAnimationSpec
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.ui.unit.Density

enum class AnimationSpecEnum(
    val descriptor: String,
    val animationSpec: FloatDecayAnimationSpec,
    val distanceLowerBounce: Float,
    val distanceUpperBound: Float,
    val velocityUpperBound: Float,
    val duration: Int
) {
    SPLINE_DECAY(
        "SplineDecaySpec",
        SplineBasedFloatDecayAnimationSpec(object: Density {
            override val density: Float
                get() = 3f
            override val fontScale: Float
                get() = 3f

        }),
        0f, 2f,100f, 250
    ),
    EXPONENTIAL_DECAY(
        "FloatExponentialDecaySpec 2F",
        FloatExponentialDecaySpec(2f),
        0f, 15f,100f, 1000
    ),
    CUSTOM_DECAY(
        "CustomDecay",
        CustomFloatDecayAnimationSpec(0.5f),
        0f, 30f, 100f, 1000
    );

    override fun toString(): String {
        return "$descriptor ($distanceLowerBounce, $distanceUpperBound, ${duration}ms)"
    }

    companion object {
        fun getEnum(value: String): AnimationSpecEnum {
            return values().first { it.descriptor == value }
        }
    }
}

class CustomFloatDecayAnimationSpec(private val targetTime: Float = 0.5f) : FloatDecayAnimationSpec {

    override val absVelocityThreshold: Float get() = 0f

    override fun getTargetValue(initialValue: Float, initialVelocity: Float): Float {
        val accelerate = - initialVelocity/targetTime
        return initialValue + kotlin.math.abs(0.5f * accelerate * targetTime * targetTime)
    }

    override fun getValueFromNanos(
        playTimeNanos: Long,
        initialValue: Float,
        initialVelocity: Float
    ): Float {
        val playTimeSecond = (playTimeNanos / 1_000_000L).toFloat() / 1000
        val acceleration = - initialVelocity/targetTime
        return (initialValue + (initialVelocity * playTimeSecond)
                + (0.5f * acceleration * playTimeSecond * playTimeSecond))
    }

    override fun getDurationNanos(initialValue: Float, initialVelocity: Float): Long =
        (targetTime * 1000).toLong() *  1_000_000L

    override fun getVelocityFromNanos(
        playTimeNanos: Long,
        initialValue: Float,
        initialVelocity: Float
    ): Float {
        val playTimeSecond = (playTimeNanos / 1_000_000L).toFloat() / 1000
        val accelerate = - initialVelocity/targetTime
        return initialVelocity + accelerate * playTimeSecond
    }
}