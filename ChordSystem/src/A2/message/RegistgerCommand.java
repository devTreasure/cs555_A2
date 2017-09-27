package A2.message;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistgerCommand implements Command {
	public static final String cmd = "CMD_RegistgerCommand";

	public String ipAddress;
	public int port;
	public int id;

	public RegistgerCommand() {
	}

	public RegistgerCommand(String ipAddress, int port, int id) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.id = id;
	}

	public byte[] unpack() {
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = null;
		DataOutputStream dout = null;

		try {
			baOutputStream = new ByteArrayOutputStream();
			dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
			dout.writeInt(cmd.length());
			dout.write(cmd.getBytes());
			dout.writeInt(ipAddress.length());
			dout.write(ipAddress.getBytes());
			dout.writeInt(port);
			dout.writeInt(id);
			dout.flush();
			marshalledBytes = baOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baOutputStream.close();
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return marshalledBytes;
	}

	public void pack(DataInputStream din) {
		try {
			ipAddress = readString(din);
			port = din.readInt();
			id = din.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readString(DataInputStream din) throws IOException {
		int IP_length = din.readInt();
		byte[] IP_address = new byte[IP_length];
		din.readFully(IP_address);
		return new String(IP_address);
	}

   @Override
   public String toString() {
      return "RegistgerCommand [cmd=" + cmd + ", ipAddress=" + ipAddress + ", port=" + port
            + ", id=" + id + "]";
   }	
	
}