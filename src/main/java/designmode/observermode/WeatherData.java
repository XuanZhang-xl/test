package designmode.observermode;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

/**
 * jdk自带观察者模式
 * 没顺序, 不易扩展, 基本没人用
 * created by XUAN on 2019/04/14
 */
public class WeatherData extends Observable {

    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal pressure;

    public void measurementsChanged() {
        setChanged();
        notifyObservers();
    }

    public void setMeasurements(BigDecimal temperature, BigDecimal humidity, BigDecimal pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public BigDecimal getPressure() {
        return pressure;
    }
}
