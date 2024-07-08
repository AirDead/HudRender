#version 150 core

in vec2 position;
in vec2 texCoords;

out vec2 fragTexCoords;

void main() {
    fragTexCoords = texCoords;
    gl_Position = vec4(position, 0.0, 1.0);
}
