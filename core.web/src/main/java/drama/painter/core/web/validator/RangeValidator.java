package drama.painter.core.web.validator;

/**
 * @author murphy
 */
public class RangeValidator implements Validator {
    final float min;
    final float max;

    public RangeValidator(byte min, byte max) {
        this.min = min;
        this.max = max;
    }

    public RangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public RangeValidator(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public RangeValidator(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(String value) {
        value = trim(value);
        if (EMPTY.validate(value)) {
            return false;
        }
        try {
            float val = Float.parseFloat(value);
            return val >= min && val <= max;
        } catch (Exception e) {
            return false;
        }
    }
}
