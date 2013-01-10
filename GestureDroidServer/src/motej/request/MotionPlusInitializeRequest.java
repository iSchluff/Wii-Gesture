package motej.request;

public class MotionPlusInitializeRequest extends WriteRegisterRequest {

	public MotionPlusInitializeRequest( ) {
		// Sending 0x55 to 0xa600f0 to initialize the MotionPlus
		super(new byte[]{(byte) 0xa6,0x00,(byte) 0xf0}, new byte[]{0x55});
		//super(new byte[]{ (byte) 0xa6 , 0x00, (byte) 0xfe}, new byte[]{0x04});
		
	}

	@Override
	public byte[] getBytes() {
		return super.getBytes();
	}

}
