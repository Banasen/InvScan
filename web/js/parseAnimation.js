parseAnimation = function(animationInfo)
{
	var animation = {frames: []};
	//Each line is a string in the Array
	for (i in animationInfo)
	{
		//Check if the object has a split method (only if it's a string)
		if (animationInfo[i].split != null)
		{
			//Split the string into 2 parts. first one is the frame number, next the duration, default duration is 1. format (frameNo[*duration])
			var info = animationInfo[i].split("*");
			//Frame number is zero-indexed
			if (parseInt(info[0]) >= 0)
			{
				//Add the frame to the array. if the duration is set in the info, use that, otherwise default to 1
				animation.frames[animation.frames.length] = {sprite: parseInt(info[0]), duration: parseInt(info[1]) >= 0 ? parseInt(info[1]) : 1};
			}
		}
	}
	//If there are no frames, add a frame pointing to the first picture
	if (animation.frames.length == 0)
	{
		animation.frames[0] = {sprite: 0, duration: 1000}; //Long duration, since it doesn't matter; might as well draw less often
	}
	//Make it start with the first frame of the animation (matters for long duration animations)
	animation.currentFrame = animation.frames.length - 1; //Set the currentFrame to the last frame in the animation
	animation.currentFrameDuration = animation.frames[animation.currentFrame].duration - 1; //Set the current frame duration to the last before it's over
	
	return animation;
};
