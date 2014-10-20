package org.atinject.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

public class ValidationTest {

	public static class DataMetadataValidationProviderRegistry {
		public static Map<Map.Entry<?, ?>, DataMetadataValidationProvider<?, ?>> providers = new HashMap<>();
		static {
			providers.put(new AbstractMap.SimpleEntry<Class<?>, Class<?>>(StringConfiguration.class, StringConstraintMetadata.class), new StringHolderStringMetadataValidationProvider());
		}
		
		public static DataMetadataValidationProvider getProvider(Class<?> d, Class<?> m) {
			return providers.get(new AbstractMap.SimpleEntry<Class<?>, Class<?>>(d, m));
		}
	}
	
	public static abstract class DataMetadataValidationProvider<Data, Metadata> {
		public abstract boolean validate(Data d, Metadata m, ConstraintValidatorContext constraintContext);
		
		protected boolean buildConstraintViolationWithTemplateAndReturnFalse(ConstraintValidatorContext constraintContext, String messageTemplate) {
			constraintContext.disableDefaultConstraintViolation();
	        constraintContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
			return false;
		}
	}
	
	public static class StringHolderStringMetadataValidationProvider extends DataMetadataValidationProvider<StringConfiguration, StringConstraintMetadata> {
		@Override
		public boolean validate(StringConfiguration d, StringConstraintMetadata m, ConstraintValidatorContext constraintContext) {
			if (! m.nullable) {
				if (d.stringValue == null) {
					return buildConstraintViolationWithTemplateAndReturnFalse(constraintContext, "null");
				}
			}
			if (m.minLength != null) {
				if (d.stringValue.length() < m.minLength.intValue()) {
					return buildConstraintViolationWithTemplateAndReturnFalse(constraintContext, "length < min");
				}
			}
			if (m.maxLength != null) {
				if (d.stringValue.length() > m.maxLength.intValue()) {
					return buildConstraintViolationWithTemplateAndReturnFalse(constraintContext, "length > max");
				}
			}
			return true;
		}
	}
	
	public static class MetadataValidator implements ConstraintValidator<CheckMetadata, DataMetadataTuple<?, ?>> {
	    @Override
	    public void initialize(CheckMetadata constraintAnnotation) {
	    }
	    
	    @Override
	    public boolean isValid(DataMetadataTuple<?, ?> object, ConstraintValidatorContext constraintContext) {
	    	return DataMetadataValidationProviderRegistry.getProvider(object.d.getClass(), object.m.getClass()).validate(object.d, object.m, constraintContext);
	    }
	}
	
	@Target({ ElementType.TYPE/*, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE*/ })
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = MetadataValidator.class)
	@Documented
	public static @interface CheckMetadata {
	    String message() default "invalid";
	    Class<?>[] groups() default { };
	    Class<? extends Payload>[] payload() default { };
	}
	
	@CheckMetadata
	public static class DataMetadataTuple<Data, Metadata> {
		Data d;
		Metadata m;
	}
	
	public static class Configuration {
		String key;
	}
	
	public static class StringConfiguration {
		String stringValue;
	}
	
	public static class Metadata {
		String description;
	}
	
	public static class StringMetadata extends Metadata {
		String defaultValue;
		StringConstraintMetadata stringValueConstraintMetadata;
	}
	
	public static class StringConstraintMetadata {
		boolean nullable;
		Integer minLength;
		Integer maxLength;
	}
	
	public static void main(String[] args) throws Exception {
		StringConfiguration sh = new StringConfiguration(); sh.stringValue = "abc55555555555555555555555";
		StringConstraintMetadata sm = new StringConstraintMetadata();
		sm.nullable=false;
		sm.maxLength = 5;
		sm.minLength = 1;
		DataMetadataTuple smt = new DataMetadataTuple(); smt.d = sh; smt.m = sm;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<DataMetadataTuple<?, ?>>> violations = validator.validate(smt);
		
		System.out.println(violations);
	}
}
