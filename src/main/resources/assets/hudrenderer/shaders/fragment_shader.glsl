#version 150 core

in vec2 fragTexCoords;
out vec4 color;

uniform sampler2D screenTexture;
uniform float blurRadius;

void main() {
    vec2 tex_offset = 1.0 / textureSize(screenTexture, 0); // Gets size of single texel
    vec4 result = vec4(0.0);

    // Apply blur effect
    for (int x = -4; x <= 4; ++x) {
        for (int y = -4; y <= 4; ++y) {
            vec2 offset = vec2(float(x), float(y)) * tex_offset * blurRadius;
            result += texture(screenTexture, fragTexCoords + offset);
        }
    }
    color = result / 81.0; // Divide by number of samples
}
