package com.hospital.doctorService.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Doctor {
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;
	    private String specialization;
	    
	    @NotBlank(message = "Mobile Number Required")
	    @Pattern(regexp="^\\d{10}$", message="Mobile number must be 10 digit")
	    private String mobileNumber;
	    
	    @NotBlank(message = "Email Required")
	    @Column(nullable = false, unique = true)
	    @Email(message="Email invalid Format")
	    private String email;
	    private String qualification;
	    private String experience;
		
		public Doctor(Long id, String name, String specialization,
				@Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digit") String mobileNumber,
				@Email(message = "Email invalid Format") String email, String qualification, String experience) {
			super();
			this.id = id;
			this.name = name;
			this.specialization = specialization;
			this.mobileNumber = mobileNumber;
			this.email = email;
			this.qualification = qualification;
			this.experience = experience;
		}
		public Doctor() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSpecialization() {
			return specialization;
		}
		public void setSpecialization(String specialization) {
			this.specialization = specialization;
		}
		public String getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getQualification() {
			return qualification;
		}
		public void setQualification(String qualification) {
			this.qualification = qualification;
		}
		public String getExperience() {
			return experience;
		}
		public void setExperience(String experience) {
			this.experience = experience;
		}
}


