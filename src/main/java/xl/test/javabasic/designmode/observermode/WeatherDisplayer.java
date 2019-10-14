package xl.test.javabasic.designmode.observermode;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

/**
 * created by XUAN on 2019/04/14
 */
public class WeatherDisplayer implements Observer, Displayer {

    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal pressure;

    @Override
    public void display() {
        System.out.println("温度:" + temperature.toPlainString() + ", 湿度:" + humidity.toPlainString() + ", 气压:" + pressure.toPlainString());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData) {
            WeatherData weatherData = (WeatherData)o;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            this.pressure = weatherData.getPressure();
            display();
        }
    }
}
