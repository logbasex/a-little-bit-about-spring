package com.tellyouiam.alittlebitaboutspring.service;

import com.tellyouiam.alittlebitaboutspring.utils.OnboardHelper;
import com.tellyouiam.alittlebitaboutspring.utils.StringHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService{
	
	private int check(String[] arr, String... valuesToCheck) {
		int index;
		for (String element : arr) {
			for (String value : valuesToCheck) {
				if (element.toLowerCase().contains(value.toLowerCase())) {
					index = Arrays.asList(arr).indexOf(value);
					if (index != -1) {
						return index;
					}
				}
			}
		}
		return 100;
	}
	
	private List<String> getCsvData(MultipartFile multipart) throws IOException {
		InputStream is = multipart.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return this.getCsvData(br, false);
	}
	
	public  List<String> getCsvData(BufferedReader bufReader) throws IOException {
		return getCsvData(bufReader, true);
	}
	
	private List<String> getCsvData(BufferedReader bufReader, boolean ignoreHeader) throws IOException {
		List<String> data = new ArrayList<>();
		String line = null;
		int count = 0;
		while ((line = bufReader.readLine()) != null) {
			count++;
			if (ignoreHeader && count == 1)
				continue;
			data.add(line);
		}
		return data;
	}
	
	
	
	@Override
	public Object automateImportOwner(MultipartFile ownerFile) {
		try {
			List<String> csvData = this.getCsvData(ownerFile);
			StringBuilder builder = new StringBuilder();
			if (!CollectionUtils.isEmpty(csvData)) {
				
				// ---------- common cols --------------------------------------
				// OWNER_KEY (ID or EMAIL), can leave blank if ID is EMAIL
				// EMAIL
				// FINANCE EMAIL
				// FIRST NAME
				// LAST NAME
				// DISPLAY NAME
				// TYPE
				// MOBILE
				// PHONE
				// FAX
				// ADDRESS
				// SUBURB (CITY)
				// STATE
				// POSTCODE
				// COUNRTY
				// GST = "true/false" or ot "T/F" or "Y/N"
				// -------------------------------------------------------------
				
				String[] header = OnboardHelper.readCsvLine(csvData.get(0));
				
				int ownerIdIndex = check(header, "OwnerID");
				int emailIndex = check(header, "Email");
				int financeEmailIndex = check(header, "FinanceEmail");
				int firstNameIndex = check(header, "FirstName");
				int lastNameIndex = check(header, "LastName");
				int displayNameIndex = check(header, "DisplayName", "Name");
				int typeIndex = check(header, "Type");
				int mobileIndex = check(header, "Mobile");
				int phoneIndex = check(header, "Phone");
				int faxIndex = check(header, "Fax");
				int addressIndex = check(header, "Address");
				int cityIndex = check(header, "City");
				int stateIndex = check(header, "State");
				int postCodeIndex = check(header, "PostCode");
				int countryIndex = check(header, "Country");
				int gstIndex = check(header, "GST");
				
				String rowHeader = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
						"OwnerID", "Email", "FinanceEmail", "FirstName", "LastName", "DisplayName",
						"Type", "Mobile", "Phone", "Fax", "Address", "City", "State", "PostCode",
						"Country", "GST"
						);
				
				builder.append(rowHeader);
				
				csvData = csvData.stream().skip(1).collect(Collectors.toList());
				for (String line : csvData) {
					String[] r = OnboardHelper.readCsvLine(line);
					
					String ownerId = OnboardHelper.readCsvRow(r, ownerIdIndex);
					String email = OnboardHelper.readCsvRow(r, emailIndex);
					String financeEmail = OnboardHelper.readCsvRow(r, financeEmailIndex);
					String firstName = OnboardHelper.readCsvRow(r, firstNameIndex);
					String lastName = OnboardHelper.readCsvRow(r, lastNameIndex);
					String displayName = OnboardHelper.readCsvRow(r, displayNameIndex);
					String type = OnboardHelper.readCsvRow(r, typeIndex);
					
					String mobile = OnboardHelper.readCsvRow(r, mobileIndex);
					if (!StringUtils.isEmpty(mobile) && mobile.matches("^[1-9]+.*")) {
						mobile = String.format("0%s", mobile);
					}
					
					String phone = OnboardHelper.readCsvRow(r, phoneIndex);
					if (!StringUtils.isEmpty(phone) && phone.matches("^[1-9]+.*"))
						phone = String.format("0%s", phone);
					
					String fax = OnboardHelper.readCsvRow(r, faxIndex);
					String address = OnboardHelper.readCsvRow(r, addressIndex);
					String city = OnboardHelper.readCsvRow(r, cityIndex);
					String state = OnboardHelper.readCsvRow(r, stateIndex);
					String postCode = OnboardHelper.readCsvRow(r, postCodeIndex);
					String country = OnboardHelper.readCsvRow(r, countryIndex);
					String gst = OnboardHelper.readCsvRow(r, gstIndex);
					
					String rowBuilder = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
							StringHelper.csvValue(ownerId),
							StringHelper.csvValue(email),
							StringHelper.csvValue(financeEmail),
							StringHelper.csvValue(firstName),
							StringHelper.csvValue(lastName),
							StringHelper.csvValue(displayName),
							StringHelper.csvValue(type),
							StringHelper.csvValue(mobile),
							StringHelper.csvValue(phone),
							StringHelper.csvValue(fax),
							StringHelper.csvValue(address),
							StringHelper.csvValue(city),
							StringHelper.csvValue(state),
							StringHelper.csvValue(postCode),
							StringHelper.csvValue(country),
							StringHelper.csvValue(gst)
					);
					builder.append(rowBuilder);
				}
			}
			
			String path = "C:\\Users\\conta\\OneDrive\\Desktop\\data\\owner-format.csv";
			try {
				File file = new File(path);
				file.setWritable(true);
				file.setExecutable(true);
				file.setReadable(true);
				
				FileOutputStream os = new FileOutputStream(file);
				os.write(builder.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return builder;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object automateImportHorse(MultipartFile horseFile, boolean isDMYformat) {
		try {
			List<String> csvData = this.getCsvData(horseFile);
			csvData = csvData.stream().filter(org.apache.commons.lang3.StringUtils::isNotEmpty).collect(Collectors.toList());
			StringBuilder builder = new StringBuilder();
			if (!CollectionUtils.isEmpty(csvData)) {
				
				// read from horse file first, standard columns order:
				// EXTERNAL ID, can leave blank if use hash code from name as id
				// NAME
				// FOALED
				// SIRE
				// DAM
				// COLOUR
				// SEX
				// AVATAR
				// ADDED DATE
				// STATUS (active/inactive)
				// CURRENT LOCATION
				// CURRENT STATUS
				// TYPE (Race Horse/ Stallion/ Speller/ Brood Mare/ Yearling)
				// CATEGORY
				// BONUS SCHEME
				// NICK NAME
				
				String[] header = OnboardHelper.readCsvLine(csvData.get(0));
				
				int externalIdIndex = check(header, "ExternalId");
				int nameIndex = check(header, "Name", "Horse");
				int foaledIndex = check(header, "DOB", "foaled");
				int sireIndex = check(header, "Sire");
				int damIndex = check(header, "Dam");
				int colorIndex = check(header, "Color");
				int sexIndex = check(header, "Sex");
				int avatarIndex = check(header, "Avatar");
				int addedDateIndex = check(header, "");
				int activeStatusIndex = check(header, "Active Status", "ActiveStatus");
				int horseLocationIndex = check(header, "Property");
				int horseStatusIndex = check(header, "Current Status","CurrentStatus");
				int typeIndex = check(header, "Type");
				int categoryIndex = check(header, "Category");
				int bonusSchemeIndex = check(header, "Bonus Scheme", "BonusScheme", "Schemes");
				int nickNameIndex = check(header, "Nick Name", "NickName");
				
				int daysHereIndex = check(header, "Days Here", "Days");
				int weeksHereIndex = check(header, "Weeks Here", "Weeks");
				
				String rowHeader = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
						"ExternalId", "Name", "Foaled", "Sire", "Dam", "Color",
						"Sex", "Avatar", "AddedDate", "ActiveStatus/Status",
						"CurrentLocation/HorseLocation", "CurrentStatus/HorseStatus",
						"Type", "Category", "BonusScheme", "NickName"
				);
				
				builder.append(rowHeader);
				
				csvData = csvData.stream().skip(1).collect(Collectors.toList());
				for (String line : csvData) {
					String[] r = OnboardHelper.readCsvLine(line);
					
					String externalId = OnboardHelper.readCsvRow(r, externalIdIndex);
					String name = OnboardHelper.readCsvRow(r, nameIndex);
					
					String foaled = OnboardHelper.readCsvRow(r, foaledIndex);
					if(!isDMYformat) {
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						formatter.setLenient(false);
						try {
							Date date= formatter.parse(foaled);
						} catch (ParseException e) {
							//If input date is in different format or invalid.
						}
					}
					
					String sire = OnboardHelper.readCsvRow(r, sireIndex);
					String dam = OnboardHelper.readCsvRow(r, damIndex);
					String color = OnboardHelper.readCsvRow(r, colorIndex);
					String sex = OnboardHelper.readCsvRow(r, sexIndex);
					
					String avatar = OnboardHelper.readCsvRow(r, avatarIndex);
					
					
					
					String activeStatus = OnboardHelper.readCsvRow(r, activeStatusIndex);
					
					String currentLocation = OnboardHelper.readCsvRow(r, horseLocationIndex);
					String currentStatus = OnboardHelper.readCsvRow(r, horseStatusIndex);
					String type = OnboardHelper.readCsvRow(r, typeIndex);
					String category = OnboardHelper.readCsvRow(r, categoryIndex);
					String bonusScheme = OnboardHelper.readCsvRow(r, bonusSchemeIndex);
					String nickName = OnboardHelper.readCsvRow(r, nickNameIndex);
					
					String rowBuilder = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
							StringHelper.csvValue(externalId),
							StringHelper.csvValue(name),
							StringHelper.csvValue(foaled),
							StringHelper.csvValue(sire),
							StringHelper.csvValue(dam),
							StringHelper.csvValue(color),
							StringHelper.csvValue(type),
							StringHelper.csvValue(sex),
							StringHelper.csvValue(avatar),
							StringHelper.csvValue(activeStatus),
							StringHelper.csvValue(currentLocation),
							StringHelper.csvValue(currentStatus),
							StringHelper.csvValue(type),
							StringHelper.csvValue(category),
							StringHelper.csvValue(bonusScheme),
							StringHelper.csvValue(nickName)
					);
					builder.append(rowBuilder);
				}
			}
			
			String path = "C:\\Users\\conta\\OneDrive\\Desktop\\data\\owner-format.csv";
			try {
				File file = new File(path);
				file.setWritable(true);
				file.setExecutable(true);
				file.setReadable(true);
				
				FileOutputStream os = new FileOutputStream(file);
				os.write(builder.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return builder;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
