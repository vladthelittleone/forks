function initiateOddsDisplay() {
    var a = [
        4278,
        1,
        [
            [
                1, // 1 = live
                [
                    [
                        1, // id sport , 1 = football, 2 = basket, 9 = baseball , 4 hockey
                        25403,
                        [2399369, 'KFC Uerdingen 05 (n)', 'SV Waldhof Mannheim', '1.010-E032418052403', 10, '05/25/2018 01:30', '', 0, 0, 1, , {}],
                        [
                            1, 1, 29, 45, 0, 0, {}, ,
                            {
                                1: 45,
                                2: 45,
                                3: 15,
                                4: 15
                            },
                            0
                        ],
                        [
                            [6, 6, []],
                            [31576369, [1, 0, 1, 1, 2000, 0.25, 2910796], [2.00, 1.88]],
                            [31576373, [5, 0, 5, 1, 500, 0.00, 2910796], [2.33, 2.55, 3.35]]
                        ],
                        1
                    ],
                    [
                        1,
                        25403,
                        [2399370, 'SC Weiche Flensburg 08', 'Energie Cottbus', '1.010-E032418052402', 10, '05/25/2018 01:00', '', 0, 3, 1, 20, {}],
                        [1, 5, 11, 45, 0, 0, {}, 'HT', {
                            1: 45,
                            2: 45,
                            3: 15,
                            4: 15
                        },
                            0
                        ],
                        [[3, 3, []], [31576379, [1, 0, 1, 1, 2000, 0.00, 2910797], [2.29, 1.65]], [31576386, [1, 0, 1, 1, 1000, -0.25, 2910797], [1.73, 2.17]]],
                        3
                    ],
                    [
                        1,
                        185,
                        [2397837, 'AFC Eskilstuna', 'Gefle', '1.005-E017618052401', 10, '05/25/2018 01:00', '', 0, 0, 1, 7, {126: [7, 0]}],
                        [
                            2, 5, 12, 45, 0, 0,
                            {
                                312: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                311: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                310: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                309: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                308: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                307: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                306: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                126: [1, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                124: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                                123: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0]
                            }, 'HT', {
                            1: 45,
                            2: 45,
                            3: 15,
                            4: 15
                        }, 0
                        ],
                        [
                            [10, 10, []],
                            [31549098, [1, 0, 1, 1, 8000, 0.75, 2905386], [1.85, 2.07]],
                            [31549099, [1, 0, 1, 1, 5000, 1.00, 2905386], [2.28, 1.70]],
                            [31549104, [5, 0, 5, 1, 2000, 0.00, 2905386], [1.61, 3.25, 6.80]]
                        ],
                        1
                    ],


                    /*[1, 185, [2397836, 'Norrby IF', 'Helsingborgs', '1.005-E017618052402', 10, '05/25/2018 01:00', '', 0, 1, 1, 6, {126: [3, 5]}], [2, 5, 12, 45, 0, 0, {
                        311: [0, 5, 12, 45, 0, 0, , 'HT', {
                            1: 45,
                            2: 45,
                            3: 15,
                            4: 15
                        }, 0],
                        310: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        309: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        308: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        307: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        306: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        126: [1, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        124: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                        123: [0, 5, 12, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0]
                    }, 'HT', {
                        1: 45,
                        2: 45,
                        3: 15,
                        4: 15
                    }, 0], [[11, 11, []], [31549080, [1, 0, 1, 1, 8000, -0.25, 2905385], [1.83, 2.09]], [31549081, [1, 0, 1, 1, 5000, 0.00, 2905385], [2.31, 1.68]], [31549086, [5, 0, 5, 1, 2000, 0.00, 2905385], [11.00, 3.90, 1.37]]], 2], [1, 25, [2397641, 'Trelleborgs FF', 'Brommapojkarna', '1.003-E013118052401', 10, '05/25/2018 01:00', '', 2, 0, 1, 4, {126: [5, 0]}], [3, 5, 11, 45, 0, 0, {
                    310: [0, 5, 11, 45, 0, 0, , 'HT', {
                        1: 45,
                        2: 45,
                        3: 15,
                        4: 15
                    }, 0],
                    309: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    308: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    307: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    306: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    126: [1, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    124: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    123: [0, 5, 11, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0]
                }, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[12, 12, []], [31545020, [1, 0, 1, 1, 12000, 0.25, 2904896], [1.91, 2.01]], [31545021, [1, 0, 1, 1, 5000, 0.50, 2904896], [2.28, 1.70]], [31545026, [5, 0, 5, 1, 3000, 0.00, 2904896], [1.035, 10.00, 65.00]], [31545030, [1, 0, 1, 1, 3000, 0.00, 2904896], [1.47, 2.81]]], 1], [1, 25, [2397642, 'IFK Goteborg', 'Djurgardens', '1.003-E013118052402', 10, '05/25/2018 01:00', '', 0, 2, 1, 2, {126: [5, 2]}], [3, 5, 4, 45, 0, 0, {
                    312: [0, 5, 4, 45, 0, 0, , 'HT', {
                        1: 45,
                        2: 45,
                        3: 15,
                        4: 15
                    }, 0],
                    311: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    310: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    309: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    308: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    307: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    306: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    126: [1, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    124: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    123: [0, 5, 4, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0]
                }, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[12, 12, []], [31545062, [1, 0, 1, 1, 12000, 0.00, 2904897], [2.04, 1.88]], [31545063, [1, 0, 1, 1, 5000, -0.25, 2904897], [1.62, 2.42]], [31545068, [5, 0, 5, 1, 3000, 0.00, 2904897], [42.00, 9.00, 1.06]], [31545072, [1, 0, 1, 1, 3000, 0.25, 2904897], [2.58, 1.55]]], 3], [1, 25, [2397643, 'Dalkurd FF', 'Elfsborg', '1.003-E013118052403', 10, '05/25/2018 01:00', '', 0, 1, 1, 3, {126: [0, 2]}], [3, 5, 10, 45, 0, 0, {
                    311: [0, 5, 10, 45, 0, 0, , 'HT', {
                        1: 45,
                        2: 45,
                        3: 15,
                        4: 15
                    }, 0],
                    310: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    309: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    308: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    307: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    306: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    126: [1, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    124: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0],
                    123: [0, 5, 10, 45, 0, 0, , 'HT', {1: 45, 2: 45, 3: 15, 4: 15}, 0]
                }, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[13, 13, []], [31545104, [1, 0, 1, 1, 12000, 0.00, 2904898], [1.95, 1.97]], [31545105, [1, 0, 1, 1, 5000, 0.25, 2904898], [2.47, 1.60]], [31545110, [5, 0, 5, 1, 3000, 0.00, 2904898], [9.25, 3.55, 1.45]], [31545114, [1, 0, 1, 1, 3000, -0.25, 2904898], [1.60, 2.47]]], 3], [1, 41184, [2401474, 'Levski Sofia', 'Cherno More Varna', '1.014-E039218052401', 10, '05/25/2018 01:30', '', 1, 0, 1, 0, {}], [1, 1, 26, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[7, 7, []], [31615193, [1, 0, 1, 1, 500, 0.75, 2917943], [1.78, 2.06]], [31615199, [5, 0, 5, 1, 500, 0.00, 2917943], [1.11, 5.20, 34.00]]], 1], [1, 253, [2402749, 'Valbo FF (n)', 'Bollnas GIF FF', '1.013-E036818052403', 10, '05/25/2018 01:30', '', 1, 0, 1, 0, {}], [1, 1, 26, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[6, 6, []], [31633858, [1, 0, 1, 1, 500, 0.75, 2921473], [1.80, 2.04]], [31633864, [5, 0, 5, 1, 500, 0.00, 2921473], [1.16, 4.90, 18.50]]], 1], [1, 253, [2402748, 'Nosaby IF', 'Solvesborgs GoIF', '1.013-E036818052402', 10, '05/25/2018 01:00', '', 0, 0, 1, 0, {}], [1, 5, 13, 45, 0, 0, {}, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[3, 3, []], [31633849, [1, 0, 1, 1, 500, 0.25, 2921472], [1.80, 2.04]], [31633855, [5, 0, 5, 1, 500, 0.00, 2921472], [2.13, 2.47, 4.10]]], 1], [1, 1002, [2397838, 'Hammarby (w)', 'Linkopings (w)', '1.020-E046518052401', 10, '05/25/2018 01:00', '', 1, 1, 1, 0, {}], [1, 5, 11, 45, 0, 0, {}, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[3, 3, []], [31549110, [1, 0, 1, 1, 1000, -0.25, 2905387], [2.13, 1.72]], [31549114, [5, 0, 5, 1, 500, 0.00, 2905387], [3.95, 2.74, 1.99]]], 2], [1, 1002, [2397839, 'Djurgardens (w)', 'Limhamn Bunkeflo (w)', '1.020-E046518052402', 10, '05/25/2018 01:00', '', 0, 0, 1, 0, {}], [1, 5, 12, 45, 0, 0, {}, 'HT', {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[3, 3, []], [31549120, [1, 0, 1, 1, 1000, 0.25, 2905388], [2.01, 1.83]], [31549124, [5, 0, 5, 1, 500, 0.00, 2905388], [2.30, 2.81, 3.05]]], 1], [1, 9423, [2402729, 'Santos Tartu', 'Maardu Linnameeskond', '1.018-E063518052405', 10, '05/25/2018 01:30', '', 1, 1, 1, 0, {}], [1, 1, 26, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[5, 5, []], [31633418, [1, 0, 1, 1, 500, -0.50, 2921447], [1.80, 2.04]]], 2], [1, 13344, [2403180, 'VfL Wolfsburg (w) (PEN)', 'Lyon (w) (PEN)', '1.019-E056918052401C', 10, '05/24/2018 23:59', '', 0, 0, 0, 0, {}], [0, 0, 25, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[1, 1, []]], 3], [1, 13344, [2403178, 'VfL Wolfsburg (w) (ET)', 'Lyon (w) (ET)', '1.019-E056918052401A', 10, '05/24/2018 23:59', '', 0, 0, 0, 0, {}], [0, 0, 8, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0], [[5, 5, []], [31637213, [1, 0, 1, 1, 2000, -0.25, 2922237], [1.85, 1.99]], [31637217, [5, 0, 5, 1, 500, 0.00, 2922237], [6.20, 1.75, 2.58]], [31637220, [1, 0, 1, 1, 1000, 0.00, 2922237], [2.78, 1.40]]], 2], [1, 13344, [2403179, 'VfL Wolfsburg (w) (PEN)', 'Lyon (w) (PEN)', '1.019-E056918052401B', 10, '05/24/2018 23:59', '', 0, 0, 0, 0, {}], [0, 0, 25, 45, 0, 0, {}, , {
                    1: 45,
                    2: 45,
                    3: 15,
                    4: 15
                }, 0
                ],
                    [[1, 1, []], [31637236, [1, 0, 1, 1, 2000, 0.00, 2922238], [2.11, 1.80]]], 3]*/
                ],
                [], []
            ],
            [

                2, // 2 =  pre match
                [
                    [
                        1,
                        23964, // league id
                        [
                            2394606, // match id
                            'Vitoria BA', 'Sampaio Correa', '1.027-E088618052401', 10, '05/25/2018 06:00', '', 0, 0, 1, ,
                            {}
                        ],
                        , // empty т.к. не лайв, тут инфа о лайв матче по идее
                        [
                            [8, 8, []], // 8 кол-во доп ставок
                            [31486819, [1, 0, 1, 0, 500, 1.25, 2891087], [1.77, 2.07]], // знак форы = противополжный на хозяев, и = на гостей
                            [31486824, [5, 0, 5, 0, 500, 0.00, 2891087], [1.31, 4.50, 7.20]]],
                        1
                    ],
                    [
                        1,
                        48952,
                        [2397668, 'Estudiantes La Plata', 'Nacional Montevideo', '1.026-E083718052402', 10, '05/25/2018 06:15', '', 0, 0, 1, 13, {}], ,
                        [
                            // [1, 0, 1, 0, 2000, 0.75, 2905056] : 1 это тип ставки. 1 - handicap, 5 - 1x2,
                            [15, 15, []],
                            [31545747, [1, 0, 1, 0, 2000, 0.75, 2905056], [2.26, 1.71]],
                            [31545752, [5, 0, 5, 0, 1500, 0.00, 2905056], [1.94, 3.35, 3.80]],
                            [31545760, [1, 0, 1, 0, 3000, 0.50, 2905056], [1.95, 1.97]]
                        ],
                        1
                    ],


                    [1, 48952, [2397669, 'Club Bolivar', 'Delfin Manta', '1.026-E083718052403', 10, '05/25/2018 08:30', '', 0, 0, 1, 18, {}], , [[15, 15, []], [31545762, [1, 0, 1, 0, 3000, 1.25, 2905057], [2.04, 1.88]], [31545767, [5, 0, 5, 0, 1500, 0.00, 2905057], [1.43, 4.40, 6.60]], [31545775, [1, 0, 1, 0, 2000, 1.00, 2905057], [1.75, 2.20]]], 1], [1, 48952, [2397670, 'Atletico Nacional Medellin', 'Colo Colo', '1.026-E083718052404', 10, '05/25/2018 08:30', '', 0, 0, 1, 12, {}], , [[15, 15, []], [31545777, [1, 0, 1, 0, 3000, 0.75, 2905058], [1.98, 1.94]], [31545782, [5, 0, 5, 0, 1500, 0.00, 2905058], [1.75, 3.15, 5.20]], [31545790, [1, 0, 1, 0, 2000, 0.50, 2905058], [1.75, 2.20]]], 1], [1, 48952, [2397671, 'Corinthians', 'Millonarios Bogota', '1.026-E083718052405', 10, '05/25/2018 08:30', '', 0, 0, 1, 11, {}], , [[15, 15, []], [31545792, [1, 0, 1, 0, 2000, 1.00, 2905059], [2.25, 1.72]], [31545797, [5, 0, 5, 0, 1500, 0.00, 2905059], [1.65, 3.60, 5.20]], [31545805, [1, 0, 1, 0, 3000, 0.75, 2905059], [1.88, 2.04]]], 1], [1, 48952, [2397672, 'Independiente', 'Deportivo Lara', '1.026-E083718052406', 10, '05/25/2018 08:30', '', 0, 0, 1, 15, {}], , [[14, 14, []], [31545807, [1, 0, 1, 0, 2000, 1.75, 2905060], [1.77, 2.17]], [31545812, [5, 0, 5, 0, 1500, 0.00, 2905060], [1.18, 6.20, 14.50]], [31545820, [1, 0, 1, 0, 3000, 2.00, 2905060], [2.04, 1.88]]], 1], [1, 48952, [2397667, 'Santos', 'Real Garcilaso', '1.026-E083718052401', 10, '05/25/2018 06:15', '', 0, 0, 1, 16, {}], , [[12, 12, []], [31545732, [1, 0, 1, 0, 2000, 2.25, 2905055], [2.04, 1.88]], [31545737, [5, 0, 5, 0, 1000, 0.00, 2905055], [1.14, 7.60, 13.50]]], 1], [1, 38809, [2381080, 'Botosani', 'ACS Poli Timisoara', '1.009-E030118052402', 10, '05/25/2018 02:00', '', 0, 0, 1, 14, {}], , [[8, 8, []], [31290373, [1, 0, 1, 0, 2000, 0.50, 2857383], [1.76, 2.13]], [31290377, [5, 0, 5, 0, 1000, 0.00, 2857383], [1.75, 3.35, 4.00]]], 1], [1, 45830, [2402341, 'Silkeborg', 'Esbjerg', '1.006-E025718052202', 10, '05/25/2018 02:15', '', 0, 0, 1, 9, {}], , [[15, 15, []], [31628539, [1, 0, 1, 0, 5000, 0.25, 2920737], [1.98, 1.94]], [31628543, [5, 0, 5, 0, 2000, 0.00, 2920737], [2.20, 3.30, 3.10]], [31631844, [1, 0, 1, 0, 3000, 0.50, 2920737], [2.21, 1.74]]], 1], [1, 20077, [2400439, 'Virtus Entella', 'Ascoli Picchio', '1.004-E016418052401', 10, '05/25/2018 02:30', '', 0, 0, 1, 5, {}], , [[22, 22, []], [31595044, [1, 0, 1, 0, 5000, 0.25, 2914234], [1.72, 2.25]], [31595045, [1, 0, 1, 0, 8000, 0.50, 2914234], [2.03, 1.89]], [31595051, [5, 0, 5, 0, 3000, 0.00, 2914234], [2.02, 3.00, 3.95]], [31647985, [1, 0, 1, 0, 3000, 0.75, 2914234], [2.35, 1.66]]], 1], [1, 3883, [2395481, 'Leiknir Reykjavik', 'IR Reykjavik', '1.012-E035218052402', 10, '05/25/2018 03:15', '', 0, 0, 1, 0, {}], , [[8, 8, []], [31512611, [1, 0, 1, 0, 2000, 0.50, 2899248], [2.07, 1.81]], [31512615, [5, 0, 5, 0, 1000, 0.00, 2899248], [2.06, 3.50, 3.25]]], 1], [1, 3883, [2395480, 'Throttur Reykjavik', 'HK Kopavogur', '1.012-E035218052401', 10, '05/25/2018 03:15', '', 0, 0, 1, 0, {}], , [[8, 8, []], [31512598, [1, 0, 1, 0, 2000, 0.25, 2899247], [2.12, 1.77]], [31512602, [5, 0, 5, 0, 1000, 0.00, 2899247], [2.46, 3.20, 2.77]]], 1], [1, 51287, [2401439, 'FUS Rabat (n)', 'Al Salmiya', '1.023-E156418052401', 10, '05/25/2018 03:00', '', 0, 0, 1, 0, {}], , [[7, 7, []], [31613234, [1, 0, 1, 0, 500, 2.00, 2917716], [2.07, 1.75]], [31613239, [5, 0, 5, 0, 500, 0.00, 2917716], [1.21, 5.80, 8.50]]], 1]
                ],
                [], []
            ]
        ],
        {
            1: 29,
            2: 27,
            3: 69,
            4: 86,
            5: 18,
            6: 2,
            7: 1,
            8: 42,
            9: 274
        },
        {
            3: 45,
            1: 46,
            9: 18,
            7: 20,
            5: 25,
            8: 16,
            2: 18,
            6: 13,
            4: 13,
            14: 8,
            15: 13,
            99: 8,
            16: 4,
            17: 0,
            13: 8,
            10: 8
        },
        0
    ];
}