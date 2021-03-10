#version 400

in vec3 inColor;
in vec3 inPosition;

out vec3 color;
uniform mat4 u_modelMat44;
uniform mat4 u_viewMat44;
uniform mat4 u_projectionMat44;

void main()
{
	color = inColor;
	vec4 modelPos = u_modelMat44 * vec4( inPosition, 1.0 );
    vec4 viewPos  = u_viewMat44 * modelPos;
	gl_Position = u_projectionMat44 * viewPos;
    //gl_Position = viewPos;
}