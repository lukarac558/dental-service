import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, Observable, of } from 'rxjs';

import { DoctorSearch, DoctorShort } from '../models/doctor.model';
import { Gender } from '../models/gender.model';
import { CustomPageCriteria, Page } from '../models/page.model';

@Injectable({
    providedIn: 'root'
})
export class DoctorsService {
    constructor(private _http: HttpClient) { }

    getDoctors(searchCriteria: CustomPageCriteria<DoctorSearch>): Observable<Page<DoctorShort>> {
        return of({
            itemsCount: 6,
            items: [{
                id: 1,
                firstName: 'Alan',
                lastName: 'Kwieciński',
                gender: Gender.Male,
                specialization: 'Stomatolog',
                aboutMe: 'Absolwent Śląskiego Uniwersytetu Medycznego w Katowicach. Specjalizuje się w leczeniu zachowawczym i protetycznym, ale wykonuje również zabiegi z zakresu endodoncji, chirurgii stomatologicznej oraz stomatologii dziecięcej. Poza gabinetem miłośnik gór i pływania.',
                services: [{
                    id: 1,
                    name: 'Konsultacja stomatologiczna'
                }, {
                    id: 2,
                    name: 'Wybielanie zębów'
                }, {
                    id: 3,
                    name: 'Ekstrakcja zęba'
                }]
            }, {
                id: 2,
                firstName: 'Agata',
                lastName: 'Fąk',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
                services: [{
                    id: 1,
                    name: 'Konsultacja stomatologiczna'
                }, {
                    id: 4,
                    name: 'Leczenie próchnicy'
                }, {
                    id: 5,
                    name: 'Pakiet higienizacyjny'
                }]
            }, {
                id: 3,
                firstName: 'Michał',
                lastName: 'Baron',
                gender: Gender.Male,
                specialization: 'Ortodonta',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
                services: [{
                    id: 6,
                    name: 'Konsultacja ortodontyczna'
                }]
            }, {
                id: 4,
                firstName: 'Agnieszka',
                lastName: 'Podlaska',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
                services: [{
                    id: 1,
                    name: 'Konsultacja stomatologiczna'
                }, {
                    id: 4,
                    name: 'Leczenie próchnicy'
                }]
            }, {
                id: 5,
                firstName: 'Maciek',
                lastName: 'Macalski',
                gender: Gender.Male,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
                services: []
            }, {
                id: 6,
                firstName: 'Katarzyna',
                lastName: 'Szczecińska',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
                services: []
            }]
        }).pipe(delay(500));
    }

}
