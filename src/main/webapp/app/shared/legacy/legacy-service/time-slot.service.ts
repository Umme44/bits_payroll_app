import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import days from 'dayjs/esm';
import { ITimeSlot } from '../legacy-model/time-slot.model';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<ITimeSlot>;
type EntityArrayResponseType = HttpResponse<ITimeSlot[]>;

@Injectable({ providedIn: 'root' })
export class TimeSlotService {
  public resourceUrl = SERVER_API_URL + 'api/attendance-mgt/time-slots';
  public commonResourceUrl = SERVER_API_URL + '/api/common/time-slots';

  constructor(protected http: HttpClient) {}

  create(timeSlot: ITimeSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timeSlot);
    return this.http
      .post<ITimeSlot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(timeSlot: ITimeSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timeSlot);
    return this.http
      .put<ITimeSlot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITimeSlot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITimeSlot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAll(): Observable<EntityArrayResponseType> {
    return this.http
      .get<ITimeSlot[]>(this.resourceUrl + '/all', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  checkTitle(title: String): Observable<HttpResponse<Boolean>> {
    return this.http.get<Boolean>(`${this.resourceUrl}/check-duplicate-title/${title}`, { observe: 'response' });
  }

  protected convertDateFromClient(timeSlot: ITimeSlot): ITimeSlot {
    const copy: ITimeSlot = Object.assign({}, timeSlot, {
      inTime: timeSlot.inTime && timeSlot.inTime.isValid() ? timeSlot.inTime.toJSON() : undefined,
      outTime: timeSlot.outTime && timeSlot.outTime.isValid() ? timeSlot.outTime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inTime = res.body.inTime ? days(res.body.inTime) : undefined;
      res.body.outTime = res.body.outTime ? days(res.body.outTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((timeSlot: ITimeSlot) => {
        timeSlot.inTime = timeSlot.inTime ? days(timeSlot.inTime) : undefined;
        timeSlot.outTime = timeSlot.outTime ? days(timeSlot.outTime) : undefined;
      });
    }
    return res;
  }

  getAllTimeSlots(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITimeSlot[]>(this.commonResourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
}
