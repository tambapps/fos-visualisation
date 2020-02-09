#version 120

uniform int green; // uniform variables can be shared with the program

void main() {
gl_FragColor = vec4(1, green, 0, 1);
}