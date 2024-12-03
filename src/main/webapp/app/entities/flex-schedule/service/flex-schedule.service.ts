import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFlexSchedule, NewFlexSchedule } from '../flex-schedule.model';

export type PartialUpdateFlexSchedule = Partial<IFlexSchedule> & Pick<IFlexSchedule, 'id'>;

type RestOf<T extends IFlexSchedule | NewFlexSchedule> = Omit<T, 'effectiveDate' | 'inTime' | 'outTime'> & {
  effectiveDate?: string | null;
  inTime?: string | null;
  outTime?: string | null;
};

export type RestFlexSchedule = RestOf<IFlexSchedule>;

export type NewRestFlexSchedule = RestOf<NewFlexSchedule>;

export type PartialUpdateRestFlexSchedule = RestOf<PartialUpdateFlexSchedule>;

export type EntityResponseType = HttpResponse<IFlexSchedule>;
export type EntityArrayResponseType = HttpResponse<IFlexSchedule[]>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/flex-schedules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getFlexScheduleByEffectiveDates(flexSchedule: IFlexSchedule, req?: any): Observable<EntityArrayResponseType> {
    return this.http.post<IFlexSchedule[]>(`${this.resourceUrl}/search`, flexSchedule, { params: req, observe: 'response' });
  }

  exportAsFlexScheduleDataInXL(): Observable<Blob> {
    return this.http.get(SERVER_API_URL + 'api/payroll-mgt/flex-schedule-export', { responseType: 'blob' });
  }

  exportAsMissingFlexScheduleDataInXL(): Observable<Blob> {
    return this.http.get(SERVER_API_URL + 'api/payroll-mgt/missing-flex-schedule-export', { responseType: 'blob' });
  }


  create(flexSchedule: NewFlexSchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexSchedule);
    return this.http
      .post<RestFlexSchedule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(flexSchedule: IFlexSchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexSchedule);
    return this.http
      .put<RestFlexSchedule>(`${this.resourceUrl}/${this.getFlexScheduleIdentifier(flexSchedule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }
  partialUpdate(flexSchedule: PartialUpdateFlexSchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexSchedule);
    return this.http
      .patch<RestFlexSchedule>(`${this.resourceUrl}/${this.getFlexScheduleIdentifier(flexSchedule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFlexSchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFlexSchedule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFlexScheduleIdentifier(flexSchedule: Pick<IFlexSchedule, 'id'>): number {
    return flexSchedule.id;
  }

  compareFlexSchedule(o1: Pick<IFlexSchedule, 'id'> | null, o2: Pick<IFlexSchedule, 'id'> | null): boolean {
    return o1 && o2 ? this.getFlexScheduleIdentifier(o1) === this.getFlexScheduleIdentifier(o2) : o1 === o2;
  }

  addFlexScheduleToCollectionIfMissing<Type extends Pick<IFlexSchedule, 'id'>>(
    flexScheduleCollection: Type[],
    ...flexSchedulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const flexSchedules: Type[] = flexSchedulesToCheck.filter(isPresent);
    if (flexSchedules.length > 0) {
      const flexScheduleCollectionIdentifiers = flexScheduleCollection.map(
        flexScheduleItem => this.getFlexScheduleIdentifier(flexScheduleItem)!
      );
      const flexSchedulesToAdd = flexSchedules.filter(flexScheduleItem => {
        const flexScheduleIdentifier = this.getFlexScheduleIdentifier(flexScheduleItem);
        if (flexScheduleCollectionIdentifiers.includes(flexScheduleIdentifier)) {
          return false;
        }
        flexScheduleCollectionIdentifiers.push(flexScheduleIdentifier);
        return true;
      });
      return [...flexSchedulesToAdd, ...flexScheduleCollection];
    }
    return flexScheduleCollection;
  }

  protected convertDateFromClient<T extends IFlexSchedule | NewFlexSchedule | PartialUpdateFlexSchedule>(flexSchedule: T): RestOf<T> {
    return {
      ...flexSchedule,
      effectiveDate: flexSchedule.effectiveDate?.format(DATE_FORMAT) ?? null,
      inTime: flexSchedule.inTime?.toJSON() ?? null,
      outTime: flexSchedule.outTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFlexSchedule: RestFlexSchedule): IFlexSchedule {
    return {
      ...restFlexSchedule,
      effectiveDate: restFlexSchedule.effectiveDate ? dayjs(restFlexSchedule.effectiveDate) : undefined,
      inTime: restFlexSchedule.inTime ? dayjs(restFlexSchedule.inTime) : undefined,
      outTime: restFlexSchedule.outTime ? dayjs(restFlexSchedule.outTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFlexSchedule>): HttpResponse<IFlexSchedule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFlexSchedule[]>): HttpResponse<IFlexSchedule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
