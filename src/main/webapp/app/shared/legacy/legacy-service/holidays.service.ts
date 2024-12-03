import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IHolidays } from '../legacy-model/holidays.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { createRequestOption } from '../../../core/request/request-util';
import dayjs from 'dayjs/esm';

type EntityResponseType = HttpResponse<IHolidays>;
type EntityArrayResponseType = HttpResponse<IHolidays[]>;

@Injectable({ providedIn: 'root' })
export class HolidaysService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/holidays');
  public commonUrl = this.applicationConfigService.getEndpointFor('api/common/holidays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holidays: IHolidays): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holidays);
    return this.http
      .post<IHolidays>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(holidays: IHolidays): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holidays);
    return this.http
      .put<IHolidays>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHolidays>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHolidays>(`${this.commonUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHolidays[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryByYear(year: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IHolidays[]>(`${this.resourceUrl}/get-by-year/${year}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryCommon(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHolidays[]>(this.commonUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(holidays: IHolidays): IHolidays {
    const copy: IHolidays = Object.assign({}, holidays, {
      startDate: holidays.startDate && holidays.startDate.isValid() ? holidays.startDate.format(DATE_FORMAT) : undefined,
      endDate: holidays.endDate && holidays.endDate.isValid() ? holidays.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((holidays: IHolidays) => {
        holidays.startDate = holidays.startDate ? dayjs(holidays.startDate) : undefined;
        holidays.endDate = holidays.endDate ? dayjs(holidays.endDate) : undefined;
      });
    }
    return res;
  }
}
