package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.min

private const val MORPH_ANIMATION_DURATION_MS = 200
private const val INITIAL_DELAY_MS = 400L
private const val PRE_MORPH_ROTATION_DURATION_MS = 600L
private const val POST_MORPH_VIEW_DELAY_MS = 800L
private const val SCALE_DOWN_FACTOR = 0.92f
private const val BASE_ROTATION_SPEED_DPS = 144f
private const val MORPH_SPIN_ROTATION_SPEED_DPS = 360f

val defaultRounding : CornerRounding = CornerRounding(radius = 0.16f , smoothing = 1f)

val shapePool : List<RoundedPolygon> = listOf(
    RoundedPolygon(numVertices = 3 , rounding = defaultRounding) ,
    RoundedPolygon(numVertices = 4 , rounding = defaultRounding) ,
    RoundedPolygon(numVertices = 5 , rounding = defaultRounding) ,
    RoundedPolygon(numVertices = 6 , rounding = defaultRounding) ,
    RoundedPolygon(numVertices = 8 , rounding = defaultRounding) ,
    RoundedPolygon.star(numVerticesPerRadius = 2 , innerRadius = 0.5f , rounding = defaultRounding) ,
    RoundedPolygon.star(numVerticesPerRadius = 5 , innerRadius = 0.5f , rounding = defaultRounding) ,
    RoundedPolygon.star(numVerticesPerRadius = 7 , innerRadius = 0.6f , rounding = defaultRounding) ,
    RoundedPolygon.star(numVerticesPerRadius = 9 , innerRadius = 0.5f , rounding = defaultRounding) ,
)

@Composable
fun AnimatedMorphingShapeContainer(imageVector : ImageVector) {

    var startShape by remember { mutableStateOf(shapePool.first()) }
    var endShape by remember { mutableStateOf(shapePool.last()) }
    var morph by remember(startShape , endShape) { mutableStateOf(Morph(startShape , endShape)) }

    val progress = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    var isMorphing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(INITIAL_DELAY_MS)
        while (isActive) {
            progress.snapTo(0f)
            isMorphing = false

            delay(PRE_MORPH_ROTATION_DURATION_MS)

            isMorphing = true

            launch {
                scale.animateTo(
                    targetValue = SCALE_DOWN_FACTOR , animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessLow
                    )
                )
            }

            progress.animateTo(
                targetValue = 1f , animationSpec = tween(
                    durationMillis = MORPH_ANIMATION_DURATION_MS , easing = FastOutLinearInEasing
                )
            )

            isMorphing = false

            launch {
                scale.animateTo(
                    targetValue = 1f , animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy , stiffness = Spring.StiffnessMedium
                    )
                )
            }

            delay(POST_MORPH_VIEW_DELAY_MS)

            startShape = endShape
            var nextShapeCandidate = shapePool.random()
            while (isActive && nextShapeCandidate === startShape) {
                nextShapeCandidate = shapePool.random()
            }
            endShape = nextShapeCandidate
            morph = Morph(startShape , endShape)
        }
    }

    LaunchedEffect(isMorphing) {
        val targetRotationSpeed = if (isMorphing) {
            MORPH_SPIN_ROTATION_SPEED_DPS
        }
        else {
            BASE_ROTATION_SPEED_DPS
        }

        var lastFrameTime = withFrameNanos { it }
        while (isActive) {
            val currentFrameTime = withFrameNanos { it }
            val deltaTimeMs = (currentFrameTime - lastFrameTime) / 1_000_000L
            if (deltaTimeMs > 0) {
                val anglePerFrame = targetRotationSpeed * deltaTimeMs / 1000f
                rotation.snapTo((rotation.value + anglePerFrame) % 360f)
            }
            lastFrameTime = currentFrameTime

        }
    }

    val shape : Shape = remember(morph , progress.value) {
        CubicPathShape(morph.asCubics(progress.value))
    }

    Box(modifier = Modifier
            .size(200.dp)
            .graphicsLayer {
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondary) , contentAlignment = Alignment.Center) {

        Box(
            modifier = Modifier
                    .padding(SizeConstants.LargeSize)
                    .graphicsLayer {
                        rotationZ = - rotation.value
                    }) {
            Icon(
                imageVector = imageVector , contentDescription = null , modifier = Modifier.size(80.dp) , tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

class CubicPathShape(private val cubicItems : List<Cubic>) : Shape {
    private val path = Path()

    override fun createOutline(
        size : Size , layoutDirection : LayoutDirection , density : Density
    ) : Outline {
        val scale = min(size.width , size.height) / 2f
        path.reset()

        if (cubicItems.isNotEmpty()) {
            val first = cubicItems.first()
            path.moveTo(
                first.anchor0X * scale + size.width / 2f , first.anchor0Y * scale + size.height / 2f
            )
            for (c in cubicItems) {
                path.cubicTo(
                    c.control0X * scale + size.width / 2f , c.control0Y * scale + size.height / 2f , c.control1X * scale + size.width / 2f , c.control1Y * scale + size.height / 2f , c.anchor1X * scale + size.width / 2f , c.anchor1Y * scale + size.height / 2f
                )
            }
            path.close()
        }
        return Outline.Generic(path)
    }

    override fun equals(other : Any?) : Boolean {
        if (this === other) return true
        if (other !is CubicPathShape) return false
        return cubicItems == other.cubicItems
    }

    override fun hashCode() : Int {
        return cubicItems.hashCode()
    }
}