package com.hewking.halo

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Guards the float-to-dimension fix: radius attrs are dp/px dimensions and
 * must be read with getDimension. getFloat on a dimension attr drops the
 * pixel value, so XML radii such as 60dp no longer match the drawn halo.
 */
class HaloProgressAttributeContractTest {

    @Test
    fun radiusAttributesAreDeclaredAndReadAsDimensions() {
        val viewSource = locateProjectFile(
            "library/src/main/java/com/hewking/halo/HaloProgressView.kt",
            "src/main/java/com/hewking/halo/HaloProgressView.kt"
        ).readText()
        val attrsSource = locateProjectFile(
            "library/src/main/res/values/attr_halo_progress.xml",
            "src/main/res/values/attr_halo_progress.xml"
        ).readText()

        assertTrue(
            attrsSource.contains(Regex("""name="outRadius"\s+format="dimension""""))
        )
        assertTrue(
            attrsSource.contains(Regex("""name="innRaduus"\s+format="dimension""""))
        )
        assertTrue(
            attrsSource.contains(Regex("""name="progress"\s+format="integer""""))
        )
        assertFalse(
            attrsSource.contains(Regex("""name="outRadius"\s+format="float""""))
        )
        assertFalse(
            attrsSource.contains(Regex("""name="innRaduus"\s+format="float""""))
        )

        assertTrue(
            viewSource.contains(Regex("""getDimension\(\s*R\.styleable\.HaloProgressView_outRadius\b"""))
        )
        assertTrue(
            viewSource.contains(Regex("""getDimension\(\s*R\.styleable\.HaloProgressView_innRaduus\b"""))
        )
        assertTrue(
            viewSource.contains(Regex("""getInteger\(\s*R\.styleable\.HaloProgressView_progress\b"""))
        )
        assertFalse(
            viewSource.contains(Regex("""getFloat\(\s*R\.styleable\.HaloProgressView_outRadius\b"""))
        )
        assertFalse(
            viewSource.contains(Regex("""getFloat\(\s*R\.styleable\.HaloProgressView_innRaduus\b"""))
        )
    }

    private fun locateProjectFile(vararg relativePaths: String): File {
        var dir: File? = File(System.getProperty("user.dir")).absoluteFile
        while (dir != null) {
            for (relativePath in relativePaths) {
                val candidate = File(dir, relativePath)
                if (candidate.isFile) {
                    return candidate
                }
            }
            dir = dir.parentFile
        }
        throw IllegalStateException(
            "Unable to locate ${relativePaths.toList()} from ${System.getProperty("user.dir")}"
        )
    }
}
