import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { Dayjs } from 'dayjs/esm';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IAttendanceEntry } from '../legacy/legacy-model/attendance-entry.model';
import { IAttendance } from '../legacy/legacy-model/attendance.model';

type EntityResponseType = HttpResponse<IAttendanceEntry>;
type EntityArrayResponseType = HttpResponse<IAttendanceEntry[]>;

@Injectable({ providedIn: 'root' })
export class UserAttendanceEntryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/user-attendance-entry');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(attendanceEntry: IAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceEntry);
    return this.http
      .post<IAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(attendanceEntry: IAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceEntry);
    return this.http
      .put<IAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  onlinePunch(): Observable<boolean> {
    return this.http.put<boolean>(this.resourceUrl + '/online-punch', { observe: 'response' });
  }

  isWFHEnabled(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + '/is-online-punch-enabled', { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAttendanceEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findInOrOutTimeByDate(date: Dayjs): Observable<EntityResponseType> {
    return this.http
      .get<IAttendanceEntry>(`${this.resourceUrl}/date/${date.format(DATE_FORMAT)}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttendanceEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  todayStatus(req?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttendanceEntry>(this.applicationConfigService.getEndpointFor('api/user-attendance-entry/status'), {
        params: options,
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(attendanceEntry: IAttendanceEntry): IAttendanceEntry {
    const copy: IAttendanceEntry = Object.assign({}, attendanceEntry, {
      date: attendanceEntry.date && attendanceEntry.date.isValid() ? attendanceEntry.date.format(DATE_FORMAT) : undefined,
      inTime: attendanceEntry.inTime && attendanceEntry.inTime.isValid() ? attendanceEntry.inTime.toJSON() : undefined,
      outTime: attendanceEntry.outTime && attendanceEntry.outTime.isValid() ? attendanceEntry.outTime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.inTime = res.body.inTime ? dayjs(res.body.inTime) : undefined;
      res.body.outTime = res.body.outTime ? dayjs(res.body.outTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((attendanceEntry: IAttendanceEntry) => {
        attendanceEntry.date = attendanceEntry.date ? dayjs(attendanceEntry.date) : undefined;
        attendanceEntry.inTime = attendanceEntry.inTime ? dayjs(attendanceEntry.inTime) : undefined;
        attendanceEntry.outTime = attendanceEntry.outTime ? dayjs(attendanceEntry.outTime) : undefined;
      });
    }
    return res;
  }

  protected getCurrentDaysAttendanceOfCurrentEmployee(): Observable<HttpResponse<IAttendance>> {
    return this.http.get<IAttendance>(`${this.resourceUrl}/isExist`, { observe: 'response' });
  }
}
