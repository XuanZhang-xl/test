package xl.test.encryption;

import org.junit.Test;

/**
 * created by zhangxuan9 on 2019/2/25
 */
public class EncryptTest {


    @Test
    public void encrypt () throws Exception {
        String encryptStr = "IYIJGUkYLnSURBPnsNcIy9UpEBVDPvnkvX8g/z4jFj1q5du0YafXpzmszMKN4KO+zPKozhSd/1Bq5du0YafXp5wC1ITDwJV9+TwmAoEim2gfsLkTUfCHCSCTpcjWZIor0CSIhOejKeBrdrf/dU10TOXxgdlQrTMEoreCm5xByjUABpsTo1JjE6aq9vwQ2ZyOND7vIwkc6qLJhQPPNo9MCQFVL/BYTgcHMcga0D0OCnasfMU2gXFQX8YtolS4nFFGxLeu9VSWiXoZlE/HRcaXZXnvP4fh7NpaLaihZ0PA/sQcylaAZzvHOcRC33b/JqqQsUbuCPom/jh6HTvaLI5mgPYcDK1rGtN2O+S3vAZpOO34qgZ1gapCWaIDCb4pWHCSu1aO3WM57TqX4fq7ii0tSwzq1120p1T4M5vIS/JILcFlZz1P2e1FYwqH+g7DQyxrBdFulG7MvFm7hPS30Abc+btWjt1jOe06cTSMZtvdL3nmFE36qDotMr5l9U4AX8h4kvjBwWirhGfOwTyjb9dx4Icj0ZqK294HlKZUgud3vWP/P4uP8ETmxANSSZo3EHXzVS5+93diHtsj99n0ASiQURfA56Xp1WMcdfvSTMuoGSsGhMXZN2mgRH4100X4xmv3R707yjOjhg5fbSBlaHxY76y5SrK8l4TsNhai2lO59POhV03CuEVzeeUbnjSroGNz+9cTM8bz01uAnCskFl9mbEilvNSIq+Dv4jUkWZk/85TutsFeBaACym3KxHsomjvzL6dfZxAXlRyuyCLDwpOA9aPSS0KMm1J/V5AB4hIrEUjZ3NjWYMwNpgqYOVPdZwhfgmPDbTQ01SEM1VIiQNwbOuWYG+VLFMGDio6zEWYjhZAfUMlXta4XgN6VMW2SPj3cRCoAi+3aQq9dXcR/PsnQ4edHz/f6im8IyAcZ/WPR14fDU0mbpXan+KlZ2rA5aY//bPIZSQ9lkKjMYSb7okZoH3n8qFhlXW7rbcrEeyiaO/Olb2fckq84CpF97hQVJ3dEw1NJm6V2p/ipWdqwOWmP/2zyGUkPZZCoczxQKIaKcXmYKxAr15IzEdnc2NZgzA2mJO5MqwP/8Memel3fU6KDSJImkx/gvVL5v24SlxiwhEK7lzIfDmZqUtjVCHoasbQqbpdeuyPQP0sCgU0fDk+1h8JX3WcOUt0GNkLU/kxPa8rRYZmClYAMM25mZ1H/m077wQOxAdcrMBmnkeDec8YYSArJ9ebYFdiJ86rczfiTEHWdmSYz7HRS4w2WHfzgdeUXJADJWt36hMYhQQt9SXoQYntkf0RvUSwCTGYw9J1LiVDtVHQng50/NopsKa/jhL0dPV8kqLp00fuj/SEHfm+tc0wwwp8XGoKnGGOuXQUosZQr8oDVfzxsgeTE9F3nRpyYJ5kAIpPVJGK+gyPiN5fMbRHwxlUFAgo4pvXppppCnXvKaLVatsQtIc+HUI/mpt3xj8PR327uchE7S9cJ+Kq8vdFOKU/y4SAe";
        String key = "f121bb2174d27f7b76682903cda672f0";
        String s = DESUtil.decryptBase64(encryptStr, key);
        System.out.println(s);

    }

    @Test
    public void decrypt () {

    }

}
