package models;

import tools.Security;

public class User {

	private int id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String hash;
	
	private String salt;
	
	private Role role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void encryptPassword() {
		if (password == null || password.isEmpty()) return;
		
		long time = System.currentTimeMillis();
		
		if (this.salt == null || this.salt.isEmpty()) {
			this.salt = Security.MD5("--" + time + "--" + getEmail() + "--");
		}
		
		this.hash = encrypt(password);
	}
	
	private String encrypt(String password) {
		return Security.MD5("--" + salt + "--" + password + "--");
	}
	
	public boolean authenticate(String password) {
		return hash.equals(encrypt(password));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}