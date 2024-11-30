package com.palash.while_spring_game_example

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var spinningWheel: SpinningWheelView

    //private lateinit var spinButton: Button
    private var currentAngle = 0f

    val number = 20106
    val numberString = number.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinningWheel = findViewById(R.id.spinningWheel)


        //spinButton = findViewById(R.id.spinButton)

        /*spinButton.setOnClickListener {
            *//*spinWheel {
                val selectedNumber = calculateSelectedNumber()//spinToStaticValue(4)
                // Do something with the selected number (e.g., show in a Toast)
                showToast("Selected Number: $selectedNumber")
            }*//*
            val staticValue = 8 // Set the desired static value (0-9)
        }*/

        lifecycleScope.launch {
            val textViews = arrayOf(
                findViewById<TextView>(R.id.digit1),
                findViewById<TextView>(R.id.digit2),
                findViewById<TextView>(R.id.digit3),
                findViewById<TextView>(R.id.digit4),
                findViewById<TextView>(R.id.digit5)
            )
            for (i in 0..4) {
                spinWheel {
                    val selectedNumber = calculateSelectedNumber()
                    textViews[i].text = selectedNumber.toString()
                    showToast("Selected Number: $selectedNumber")
                }
                //println(digit) // Print current number
                //spinToTarget(digit)
                //textViews[digit].text = numberString[digit].toString()
                delay(10000)
            }
        }



        /*lifecycleScope.launch {
            for (digit in numberString) {
                println(digit) // Print current number
                spinToTarget(digit.digitToInt())
                delay(5000) // Non-blocking delay
            }
        }*/
    }

    fun spinToTarget(targetNumber: Int) {
        val segmentCount = 10 // Total segments (0-9)
        val anglePerSegment = 360f / segmentCount // Each segment spans 36 degrees

        // Calculate target angle to align segment with pointer
        val targetAngle = 360f - (anglePerSegment * (targetNumber + 1)) + (anglePerSegment / 2)

        // Add multiple rotations for realistic spin
        val fullRotations = 5 // Number of full spins
        val finalRotation = (360 * fullRotations) + targetAngle

        // Create the spin animation
        val spinAnimation = RotateAnimation(
            0f, finalRotation,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 3000 // Spin duration in milliseconds
            fillAfter = true // Retain the position after animation
            interpolator = DecelerateInterpolator() // Slow down at the end
        }

        spinAnimation.setAnimationListener(object :
            android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {
                //spinButton.isEnabled = false // Disable button during spin
            }

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                //spinButton.isEnabled = true // Re-enable button
                val textViews = arrayOf(
                    findViewById<TextView>(R.id.digit1),
                    findViewById<TextView>(R.id.digit2),
                    findViewById<TextView>(R.id.digit3),
                    findViewById<TextView>(R.id.digit4),
                    findViewById<TextView>(R.id.digit5)
                )
                lifecycleScope.launch {
                    for (digit in numberString.indices) {
                        println(digit) // Print current number
                        //spinToTarget(digit)
                        textViews[digit].text = numberString[digit].toString()
                        delay(5200)
                    }
                }

                Toast.makeText(
                    this@MainActivity,
                    "Wheel stopped at: $targetNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })

        spinningWheel.startAnimation(spinAnimation)
    }

    private fun calculateSelectedNumber(): Int {
        val anglePerSegment = 360 / 10
        val normalizedAngle = (360 - (currentAngle % 360)).toInt() // Reverse rotation
        return (normalizedAngle / anglePerSegment) % 10
    }

    private fun spinWheel(onEnd: () -> Unit) {
        val randomAngle = (720..3600).random().toFloat() // Random spins (2 to 10 full rotations)
        val targetAngle = currentAngle + randomAngle

        val animator = ObjectAnimator.ofFloat(spinningWheel, "rotation", currentAngle, targetAngle)
        animator.duration = 5000
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationRepeat(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                currentAngle = targetAngle % 360
                //onRestart()
                onEnd()
            }
        })
        animator.start()
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}