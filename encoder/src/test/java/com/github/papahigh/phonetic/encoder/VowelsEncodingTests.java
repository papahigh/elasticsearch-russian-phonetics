/*
 * Copyright 2019 Nikolay Papakha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.papahigh.phonetic.encoder;


import org.junit.Test;


public class VowelsEncodingTests extends RussianEncoderTestCase {

    @Test
    public void testVowelsSequence() {

        // [ио] [ое]
        testEquals("алое", "алоэ");
        testEquals("алое", "олоэ");
        testEquals("период", "пириод");
        testEquals("каноэ", "каное");
        testEquals("трио", "триа");
        testEquals("период", "периат");

        // АО ОА (ОО) (АА) -> А 
        testEquals("паодаль", "поодаль");
        testEquals("черноокий", "чернаокий");
        testEquals("клоака", "клаака");
        testEquals("соавтор.", "саавтор.");
        testEquals("праотец", "проотец");
        testEquals("праотец", "проатец");
        testEquals("фараон", "фароон");
        testEquals("фараон", "фораон");
        testEquals("воображение", "воабражение");
        testEquals("пообещать", "поабещать");
        testEquals("коалиция", "коолиция");
        testEquals("индивидуум", "индивидум");
        testEquals("вакуум", "вакум");
        testEquals("вакуум", "ваакум");

        // (Ы|И|Е|Э|(А,О,Я if after sizzle))(Е|И|Э|Ы) -> Э
        testEquals("периизбранный", "переизбранный");
        testEquals("итэеровский", "итыровский"); // ИТР (инженерно­технический работник)
        testEquals("итэеровский", "итэровский");
        testEquals("переиначить", "перииначить");
        testEquals("неизмеримо", "ниизмеримо");
        testEquals("выиграть", "выеграть");
        testEquals("выиграть", "въииграть");
        testEquals("ассоцыировать", "ассоциировать");
        testEquals("ассоцыировать", "ассоцеировать");
        testEquals("прииск", "приеск");
        testEquals("прииск", "преиск");
        testEquals("диета", "деета");
        testEquals("пациент", "пацыент");
        testEquals("пациент", "пацыэнт");
        testEquals("хоккиист", "хокеист");
        testEquals("чаинка", "чиинка");
        testEquals("чаинка", "чеинка");
        testEquals("чаинка", "чяинка");
        testEquals("иррадиировать", "ирадиеровать");
        testEquals("гостеприимный", "гостепреемный");
        testEquals("гостеприимный", "гостеприемный");
        testEquals("гостеприимный", "гостепреимный");
        testEquals("нуклииновый", "нуклеиновый");
        testEquals("ниистовый", "неистовый");

        // АЕ/АЭ/ОЕ/ОЭ
        testEquals("орфоепия", "орфаепия");
        testEquals("украина", "укроина");
        testEquals("лихоимец", "лихаимец");
        testEquals("проект", "праект");
        testEquals("проект", "проэкт");
        testEquals("проект", "праэкт");
        testEquals("поэма", "поема");
        testEquals("теплаэлектроцентраль", "теплоелектроцентраль");
        testEquals("теплаэлектроцентраль", "теплаелектроцентраль");

        // АУ/ОУ
        testEquals("глАУкома", "глОУкома");
        testEquals("кАУчук", "коучук");
        testEquals("раут", "роут");
        testEquals("аул", "оул");
        testEquals("заусенец", "зоусениц");
        testEquals("паучок", "поучок");
        testEquals("заурядный", "зоурядный");

        testNOTEquals("заурядный", "зарядный");

        // ИУ/ЕУ
        testEquals("приумолкнуть", "преумолкнуть");
        testEquals("приутихнуть", "преутихнуть");
        testEquals("царЕУбица", "царИУбица");
        testEquals("линолеум", "линолиум");
        testEquals("неумейка", "ниумейка");
        testEquals("неучёный", " ниучёный");
        testEquals("неумеренный", "ниумереный");
        testEquals("радиус,", "радеус");
        testEquals("ниужто", "неужто");

        // ЕО/ЕА/ИО/ИА
        testEquals("всиобщий", "всеобщий");
        testEquals("неоткуда", "неаткуда");
        testEquals("ореал,", "ориал");
        testEquals("ореал,", "ариал");
        testEquals("перЕОбуть", "перЕАбуть");
        testEquals("переобуть", "периабуть");
        testEquals("переобуть", "периобуть");
        testEquals("переоценка", "периоценка");
        testEquals("бронеавтомобиль", "брониавтомобиль");
        testEquals("реактивный", "реоктивный");
        testEquals("милицыонер", "милицыанер");
    }

    @Test
    public void testVowels() {

        testEquals("ряды", "риды");
        testEquals("лук", "люк");
        testEquals("полка", "полька");
        testEquals("сосна", "сасна");
        testEquals("был", "быль");
        testEquals("лук", "люк");

        testEquals("пьянка", "пйанка");

        testNOTEquals("пьянка", "пьенка");

        testEquals("пятак", "питак");
        testEquals("пятак", "петак");

        testNOTEquals("пятак", "пьатак");

        testEquals("всё", "всьо");
        testEquals("всё", "всо");

        testNOTEquals("всё", "все");

        testEquals("тюк", "тук");
        testEquals("лес", "лэс");
        testEquals("ленив", "лэниф");

        testEquals("на деревьях", "на деревьйах");

        testEquals("чаща", "чяща");

        testEquals("свезной", "свизной");
        testEquals("свезной", "связной");

        testEquals("песочный", "писочный");
        testEquals("песочный", "пясочный");
        testEquals("песочный", "пысочный");

        testNOTEquals("песочный", "пасочный");
        testNOTEquals("песочный", "пусочный");
        testNOTEquals("песочный", "пёсочный");
        testNOTEquals("пёсочный", "писочный");

        testEquals("дверной", "двярной");
        testEquals("дверной", "двирной");

        testEquals("звонок", " званок");

        testNOTEquals("звонок", "звенок");
        testNOTEquals("звонок", "звынок");
        testNOTEquals("звонок", "звянок");

        testEquals("связной", "свезной");
        testEquals("песочный", "писочный");
        testEquals("песочный", "пясочный");
        testEquals("мясной", "месной");
        testEquals("тяжёлый", "тежолый");
        testEquals("тянуть", "тенуть");
        testEquals("глядит", "гледит");
        testEquals("одинокий", "оденокий");
        testEquals("весенний", "висенний");
        testEquals("кипяток", "кепиток");
        testEquals("тестировать", "тэстировать");
        testEquals("тестировать", "тыстировать");
        testEquals("шестой", "шыстой");
        testEquals("лежать", "лижать");
        testEquals("жизнь", "жызнь");
        testEquals("жёлтый", "жолтый");
        testEquals("желтеть", "жэлтэть");
        testEquals("желтеть", "жылтэть");
        testEquals("менять", "минять");
        testEquals("широкий", "шырокий");
        testEquals("лошадей", "лошедей");
        testEquals("шокировать", "шакировать");
        testEquals("шоколад", "шыкалад");
        testEquals("шоколад", "шыкалат");
        testEquals("шестой", "шыстой");
        testEquals("окно", "акно");
        testEquals("жюри", "жури");
        testEquals("корова", "карова");
        testEquals("сома", "сама");
        testEquals("циркач", "цыркач");
        testEquals("шишка", "шышка");
        testEquals("живот", "жывот");
        testEquals("цокотуха", "цыкатуха");
        testEquals("часы", "чисы");
        testEquals("шокирован", "шакираван");

        testEquals("календарь", "колендарь");
        testNOTEquals("календарь", " кылендарь");

        testEquals("этаж", "итаж");
        testEquals("этаж", "ытаж");
        testEquals("этаж", "етаж");

    }

    @Test
    public void testSofterVowelWithYotSound() {

        testEquals("айяй", "айай");

        testEquals("яд", "йад");
        testEquals("яд", "ят");
        testEquals("яд", "йат");

        testNOTEquals("яд", "ат");

        testEquals("иосиф", "ёсиф");
        testEquals("иосиф", "йосиф");

        testNOTEquals("иосиф", "ясиф");
        testNOTEquals("иосиф", "юсиф");
        testNOTEquals("иосиф", "иусиф");

        testEquals("йирусалим", "ерусалим");
        testEquals("иерусалим", "ерусалим");
        testEquals("иерусалим", "йэрусалим");
        testEquals("иерусалим", "йерусалим");

        testEquals("иордания", "ёрдания");
        testEquals("иордания", "йордания");

        testEquals("иардания", "ярдания");
        testEquals("иардания", "йардания");

        testEquals("съезд", "сйэст");
        testEquals("объявление", "обйавленье");
        testEquals("обезьяна", "обизьйана");

        testNOTEquals("иордания", "иардания");

        testEquals("объём", "абйом");
        testEquals("объем", "абйэм");

        testNOTEquals("объём", "объем");

        testEquals("чьи", "чйи");
        testEquals("лисьи", "лисйи");

        testEquals("пианино", "пеанино");
        testEquals("пианино", "пеонина");

        testEquals("пианино", "пьянина");

        testEquals("безапиляционный", "безапиляцыонный");
        testEquals("безапиляционый", "безапиляциённый");

        testEquals("павильон", "павилион");
        testEquals("павильон", "павилиён");
        testEquals("павильён", "павилиён");

        testEquals("ёж", "йош");
        testEquals("егерь", "йэгерь");
        testEquals("ёгурт", "йогурт");
        testEquals("ёгурт", "иогурт");

        testEquals("пьёт", "пйот");
        testEquals("пьёт", "пиот");
        testEquals("льют", "лйут");
        testEquals("льют", "лиут");

        testEquals("рьяный", "рьйанный");
        testEquals("майонез", "маёнез");
        testEquals("майонез", "майонес");
        testEquals("майонез", "маёнэз");
        testEquals("майонез", "майонес");
        testEquals("шуметь", "шумэть");
        testEquals("шуметь", "шумэть");

        testEquals("лаев", "лайэф");
        testEquals("лаев", "лаиэф");
        testEquals("лаев", "лаэф");
        testEquals("лиев", "леив");
        testEquals("лиев", "леев");
        testEquals("лиев", "лиив");
        testEquals("лиев", "льев");

        testEquals("лаиев", "лаев");
        testEquals("лаиев", "лаиив");
        testEquals("лаиев", "лаеев");

        testEquals("лаев", "лайэф");

        testEquals("майор", "маёр");
        testEquals("майор", "маиор");
        testEquals("майёр", "маиор");
        testEquals("маёр", "маиор");

        testEquals("маяк", "майяк");
        testEquals("маяк", "маияк");
        testEquals("маяк", "майак");
        testEquals("маяк", "маиак");

        testNOTEquals("майор", "моер");
        testNOTEquals("майор", "мыёр");

        testEquals("ёлка", "елка");

        testNOTEquals("ёлка", "олка");
        testNOTEquals("ёлка", "алка");
        testNOTEquals("ёлка", "улка");
        testNOTEquals("ёлка", "юлка");
    }
}
