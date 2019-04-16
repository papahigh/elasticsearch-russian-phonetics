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


public class ConsonantsEncodingTests extends RussianEncoderTestCase {

    @Test
    public void testConsonantVoicing() {

        // de-voicing
        testEquals("визг", "визкк");
        testEquals("визг", "виск");
        testEquals("вдрызг", "вдрыск");
        testEquals("гроздь", "грость");
        testEquals("грозд", "грость");

        testEquals("мотив", "матиф");
        testEquals("впадина", "фпадина");
        testEquals("абсурд", "апсурд");
        testEquals("абсурд", "апсурт");
        testEquals("дуб", "дуп");
        testEquals("пруд", "прут");
        testEquals("труд", "трут");
        testEquals("сад", "сат");
        testEquals("кадка", "катка");
        testEquals("автомат", "афтомат");
        testEquals("вкус", "фкус");
        testEquals("втулка", "фтулка");
        testEquals("вчера", "фчира");
        testEquals("втулка", "фтулка");
        testEquals("втулка", "фтулка");
        testEquals("Хлеб", "Хлеп");
        testEquals("цветов", "цветоф");
        testEquals("Когти", "Кокти");
        testEquals("Отбой", "Адбай");

        // voicing
        testEquals("сбор", "збор");
        testEquals("отдать", "аддать");
        testEquals("молотьба", "маладьба");
        testEquals("отбежать", "адбижать");
        testEquals("сберечь", "збиречь");
        testEquals("сбор", "збор");
        testEquals("сделать", "зделать");
        testEquals("отбор", "адбор");
        testEquals("збросить", "сбросеть");
        testEquals("просьба", "прозьба");
        testEquals("косьба", "козьба");
        testEquals("анекдот", "анегдот");
        testEquals("асбест", "азбест");
        testEquals("афганец", "авганиц");
        testEquals("вокзал", "вогзал");

        testNOTEquals("третьяков", "тредьяков");
        testNOTEquals("чувство", "чувсдво");

    }

    @Test
    public void testConsonantVoicingPairs() {

        testNOTEquals("арабы", "арапы");

        testEquals("араб", "арап");
        testNOTEquals("арабы", "арапы");

        testEquals("ад", "ат");
        testNOTEquals("аду", "ату");

        testEquals("витраж", "витраш");
        testNOTEquals("витражи", "витраши");

        testEquals("дуб", "дуп");
        testNOTEquals("дубы", "дупы");

        testEquals("мотив", "матиф");
        testNOTEquals("мотивы", "матифы");
    }


    @Test
    public void testRepeatedConsonants() {
        testEquals("студеннннтка", "студенка");
    }

    @Test
    public void testConsonantsNormalization() {

        testEquals("иноходцы", "инохоцы");
        testEquals("синего", "синива");
        // СТН → СН
        testEquals("лесница", "лестница");
        testEquals("властный", "власный");
        testEquals("прелестный", "прелесный");
        testEquals("честный", "чесный");
        testEquals("радостный", "радосный");
        testEquals("страстный", "страсный");
        testEquals("пакостник", "пакосник");
        testEquals("хрустнуть", "хруснуть");
        testEquals("уместный", "умесный");
        testEquals("костный", "косный");
        testEquals("хрустнул", "хруснул");
        testEquals("областной", "обласной");

        // СТЛ → СЛ
        testEquals("участливый", "учасливый");
        testEquals("счастливый", "счасливый");
        testEquals("завистливый", "зависливый");

        // ХГ → Г
        testEquals("бухгалтер", "бугалтер");
        testEquals("двухгодичный", "двугодичный");
        testEquals("трёхглавый", "трёглавый");
        testEquals("мягкий", "мяхкий");
        testEquals("легкий", "лехкий");

        // ВСТВ → СТВ
        testEquals("здраствуй", "сдрафствуй");
        testEquals("здравствуй", "здраствуй");
        testEquals("здрафствуй", "здраствуй");
        testEquals("чувство", "чуство");
        testEquals("шефство", "шевство");
        testEquals("шефство", "шество");
        testEquals("шеф", "шев");

        // ТЧ, ДЧ → Ч
        testEquals("летчик", "лечик");
        testEquals("отчет", "ачет");
        testEquals("отчёт", "ачот");
        testEquals("докладчик", "даклачик");
        testEquals("проходчик", "прохочик");

        // ДЦ, ДС, ТС, ТЦ, ДЦ, ТЬС → Ц
        testEquals("двадцать", "двацать");
        testEquals("золотце", "золоце");
        testEquals("отцепить", "ацыпить");
        testEquals("иноходцы", "инохоцы");
        testEquals("молодца", "молоца");
        testEquals("детство", "децтво");
        testEquals("богатство", "богацтво");
        testEquals("берётся", "бероцца");
        testEquals("учиться", "учицца");
        testEquals("рваться", "рвацца");
        testEquals("годится", "гадицца");
        testEquals("шестьсот", "шисцот");

        // ЗДЦ, СТЦ → CЦ
        testEquals("крестцовый", "кресцовый");
        testEquals("под уздцы", "под усцы");
        testEquals("под уздцы", "под узцы");
        testEquals("истца", "исца");
        testEquals("истца", "изца");

        // ДСК, TCK → ЦК
        testEquals("кисловотск", "кисловоцк");
        testEquals("кисловодск", "кисловоцк");
        testEquals("азиатский", "азиацкий");
        testEquals("городской", "гороцкой");
        testEquals("светский", "свецкий");

        // СЖ, ЗЖ → Ж
        testEquals("сжарить", "жарить");
        testEquals("разжать", "ражать");
        testEquals("позже", "пожже");
        testEquals("разжать", "ражать");
        testEquals("изжить", "ижить");
        testEquals("возжи", "вожжы");
        testEquals("уезжать", "уежать");

        // СШ, ЗШ → Ш
        testEquals("низший", "ниший");
        testEquals("высший", "выший");
        testEquals("разшуметься", "расшуметься");
        testEquals("разшуметься", "рашуметься");
        testEquals("расшуметься", "рашумецца");
        testEquals("сумасшедший", "сумашедший");

        // СЩ, ЗЧ, СЧ, ШЧ, ЖЧ, ЗДЧ, СТЧ, ТЩ → Щ
        testEquals("отщепить", "ощепить");
        testEquals("очщепить", "ощепить");
        testEquals("очшепить", "ощепить");
        testEquals("заказчик", "закасчик");
        testEquals("закасщик", "закащик");
        testEquals("счастливый", "щастливый");
        testEquals("счастливый", "сщастливый");
        testEquals("счет", "щет");
        testEquals("расчесать", "разчесать");
        testEquals("расчесать", "ращесать");
        testEquals("громоздче", "громосче");
        testEquals("объездчик", "объезчик");
        testEquals("объездчик", "объезтчик");
        testEquals("объездчик", "обйэсчик");
        testEquals("объездчик", "обйэстчик");
        testEquals("громосче", "громоще");
        testEquals("перебежчик", "перебещик");
        testEquals("веснушчатый", "веснущатый");
        testEquals("жёстче", "жёще");
        testEquals("хлёстче", "хлёще");
        testEquals("хрустче", "хруще");
        testEquals("лучше", "лутьше");
        testEquals("лучше", "лутьшэ");
        testEquals("лучше", "лучще");
        testEquals("лучше", "лутьще");

        // ЗС, СЗ → С
        testEquals("майский", "майзский");
        testEquals("рассорить", "разсорить");
        testEquals("разсылать", "рассылать");
        testEquals("рассылать", "расылать");

        // РДЦ → РЦ
        testEquals("сердце", "серце");
        // РДЧ → РЧ
        testEquals("сердчишко", "серчишко");
        // ЛНЦ, НДЦ → НЦ
        testEquals("солнце", "сонце");
        testEquals("голландцы", "голланцы");

        // СТСК → СК
        testEquals("марксистский", "марксиский");

        // СТК, СДК, ЗТК, ЗДК → СК
        testEquals("невестка", "невеска");
        testEquals("повестка", "повеска");
        testEquals("машинистка", "машинизтка");
        testEquals("машинизтка", "машиниска");
        testEquals("машинистка", "машиниска");
        testEquals("шёрстка", "шорска");
        testEquals("жёстко", "жоска");
        testEquals("жёсдко", "жоска");
        testEquals("поездка", "поеска");
        testEquals("громоздкий", "громоский");
        testEquals("бороздка", "бороска");
        testEquals("бюздгалтер", "бюсгалтер");
        testEquals("громоздкий", "громоский");

        // СТГ, ЗТГ, СДГ, ЗДГ → ЗГ
        testEquals("бюстгалтер", "бюсгалтер");
        testEquals("бюстгалтер", "бюзгалтер");
        testEquals("бюстгалтер", "бюсзгалтер");
        testEquals("бюстгалтер", "бюстгалтер");
        testEquals("бюстгалтер", "бюсдгалтер");
        testEquals("бюстгалтер", "бюзтгалтер");
        testEquals("бюстгалтер", "бюздгалтер");

        // ЗДН → ЗН
        testEquals("поздно", "позно");
        testEquals("поздний", "позний");
        testEquals("звездный", "звезный");
        testEquals("звёздный", "звёзный");

        testNOTEquals("звездный", "звёздный");
        testNOTEquals("звезда", "звёзда");

        // НДШ, НТШ → НШ
        testEquals("ландшафт", "ланшафт");
        testEquals("лантшафт", "ланшафт");
        testEquals("мундштук", "мунштук");
        testEquals("эндшпиль", "эншпиль");

        // НДСК → НСК
        testEquals("голландский", "голланский");

        // НТСК → НСК
        testEquals("гигантский", "гиганский");

        // НТСТВ → НСТВ
        testEquals("агентство", "агенство");

        // НДК → НК
        testEquals("ирландка", "ирланка");
        testEquals("шотландка", "шотланка");

        // НТГ → НГ
        testEquals("рентген", "ренген");

        // НТК → НК
        testEquals("лаборантка", "лаборанка");
        testEquals("студентка", "студенка");
        testEquals("пациентка", "пациенка");

        // ЧН → ШН
        testEquals("конечно", "канешна");
        testEquals("алчный", "алшный");
        testEquals("беспечный", "беспешный");
        testEquals("фоминична", "фоминишна");
        testEquals("саввична", "саввишна");
        testEquals("ильинична", "ильинишна");
        testEquals("прачечная", "прачешная");
        testEquals("скворечник", "скворешник");
        testEquals("копеечный", "копеешный");
        testEquals("молочный", "молошный");
        testEquals("сердечный удар", "сердешный удар");

        // ЧТ → ШТ
        testEquals("что", "што");
        testEquals("чтобы", "штобы");
        testEquals("нечто", "нэчто");
        testEquals("нечто", "нешто");

        // ЖК → ШК
        testEquals("вперемежку", "вперемешку");
        testEquals("книжка", "книшка");
        testEquals("ложка", "лошка");

        // ОГО, ЕГО → АВА, ЭВ
        testEquals("золотого", "золотова");
        testEquals("золотого", "залатова");
        testEquals("хорошего", "харошева");
        testEquals("итого", "итаво");

    }

    @Test
    public void testConsistencyWithVowels() {
        testEquals("годится", "гадицца");
        testEquals("годиться", "гадицца");

        testEquals("берёдся", "бероцца");
        testEquals("берёдься", "бероцца");
    }
}
