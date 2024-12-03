import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { IDateRangeDTO } from '../../model/DateRangeDTO';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import { IAttendanceEntry } from '../legacy-model/attendance-entry.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IManualAttendanceEntry } from '../legacy-model/manual-attendance-entry.model';

type EntityResponseType = HttpResponse<IManualAttendanceEntry>;
type EntityArrayResponseType = HttpResponse<IManualAttendanceEntry[]>;

@Injectable({ providedIn: 'root' })
export class ManualAttendanceEntryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/manual-attendance-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IManualAttendanceEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllByDateRangeForCurrentEmployee(filter: Filter): Observable<EntityArrayResponseType> {
    return this.http.post<IManualAttendanceEntry[]>(this.resourceUrl + '/find-by-date-range', filter, { observe: 'response' });
  }

  findConflictByDateRangeForCurrentEmployee(dateRangeDTO: IDateRangeDTO): Observable<HttpResponse<boolean>> {
    const copy = this.convertDateRangeFromClient(dateRangeDTO);
    return this.http.post<boolean>(this.resourceUrl + '/conflict-between-date-range', dateRangeDTO, { observe: 'response' });
  }

  create(manualAttendanceEntry: IManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .post<IManualAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  applyAndApproveByHR(manualAttendanceEntry: IManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .post<IManualAttendanceEntry>(this.resourceUrl + '/apply-and-approve-by-hr', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findInOrOutTimeByDateAndEmployeeId(date: any, employeeId: number): Observable<HttpResponse<IAttendanceEntry>> {
    return this.http
      .get<IAttendanceEntry>(`${this.resourceUrl}/date-wise-entry-search/${date.format(DATE_FORMAT)}/${employeeId}`, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(manualAttendanceEntry: IManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .put<IManualAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateRangeFromClient(dateRangeDTO: IDateRangeDTO): IDateRangeDTO {
    const copy: IDateRangeDTO = Object.assign({}, dateRangeDTO, {
      startDate: dateRangeDTO.startDate && dateRangeDTO.startDate.isValid() ? dateRangeDTO.startDate.format(DATE_FORMAT) : undefined,
      endDate: dateRangeDTO.endDate && dateRangeDTO.endDate.isValid() ? dateRangeDTO.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromClient(manualAttendanceEntry: IManualAttendanceEntry): IManualAttendanceEntry {
    const copy: IManualAttendanceEntry = Object.assign({}, manualAttendanceEntry, {
      date: manualAttendanceEntry.date && manualAttendanceEntry.date.isValid() ? manualAttendanceEntry.date.format(DATE_FORMAT) : undefined,
      inTime: manualAttendanceEntry.inTime && manualAttendanceEntry.inTime.isValid() ? manualAttendanceEntry.inTime.toJSON() : undefined,
      outTime:
        manualAttendanceEntry.outTime && manualAttendanceEntry.outTime.isValid() ? manualAttendanceEntry.outTime.toJSON() : undefined,
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
      res.body.forEach((manualAttendanceEntry: IManualAttendanceEntry) => {
        manualAttendanceEntry.date = manualAttendanceEntry.date ? dayjs(manualAttendanceEntry.date) : undefined;
        manualAttendanceEntry.inTime = manualAttendanceEntry.inTime ? dayjs(manualAttendanceEntry.inTime) : undefined;
        manualAttendanceEntry.outTime = manualAttendanceEntry.outTime ? dayjs(manualAttendanceEntry.outTime) : undefined;
      });
    }
    return res;
  }
}
