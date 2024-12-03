import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFlexScheduleApplication, NewFlexScheduleApplication } from '../flex-schedule-application.model';
import { calculateDurationOnDays } from '../../../shared/util/date-util';

export type PartialUpdateFlexScheduleApplication = Partial<IFlexScheduleApplication> & Pick<IFlexScheduleApplication, 'id'>;

type RestOf<T extends IFlexScheduleApplication | NewFlexScheduleApplication> = Omit<
  T,
  'effectiveFrom' | 'effectiveTo' | 'sanctionedAt' | 'appliedAt' | 'createdAt' | 'updatedAt'
> & {
  effectiveFrom?: string | null;
  effectiveTo?: string | null;
  sanctionedAt?: string | null;
  appliedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestFlexScheduleApplication = RestOf<IFlexScheduleApplication>;

export type NewRestFlexScheduleApplication = RestOf<NewFlexScheduleApplication>;

export type PartialUpdateRestFlexScheduleApplication = RestOf<PartialUpdateFlexScheduleApplication>;

export type EntityResponseType = HttpResponse<IFlexScheduleApplication>;
export type EntityArrayResponseType = HttpResponse<IFlexScheduleApplication[]>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/flex-schedule-applications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(flexScheduleApplication: NewFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .post<RestFlexScheduleApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(flexScheduleApplication: IFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .put<RestFlexScheduleApplication>(`${this.resourceUrl}/${this.getFlexScheduleApplicationIdentifier(flexScheduleApplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(flexScheduleApplication: PartialUpdateFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .patch<RestFlexScheduleApplication>(
        `${this.resourceUrl}/${this.getFlexScheduleApplicationIdentifier(flexScheduleApplication)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFlexScheduleApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFlexScheduleApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  exportAsFlexScheduleReportDataInXLByDateRange(req?: any): Observable<Blob> {
    Object.keys(req).forEach(k => (req[k] === null || req[k] === undefined) && delete req[k]);
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/payroll-mgt/flex-schedule-applications-export-report', {
      params: options,
      responseType: 'blob',
    });
  }

  getFlexScheduleApplicationIdentifier(flexScheduleApplication: Pick<IFlexScheduleApplication, 'id'>): number {
    return flexScheduleApplication.id;
  }

  compareFlexScheduleApplication(
    o1: Pick<IFlexScheduleApplication, 'id'> | null,
    o2: Pick<IFlexScheduleApplication, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getFlexScheduleApplicationIdentifier(o1) === this.getFlexScheduleApplicationIdentifier(o2) : o1 === o2;
  }

  addFlexScheduleApplicationToCollectionIfMissing<Type extends Pick<IFlexScheduleApplication, 'id'>>(
    flexScheduleApplicationCollection: Type[],
    ...flexScheduleApplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const flexScheduleApplications: Type[] = flexScheduleApplicationsToCheck.filter(isPresent);
    if (flexScheduleApplications.length > 0) {
      const flexScheduleApplicationCollectionIdentifiers = flexScheduleApplicationCollection.map(
        flexScheduleApplicationItem => this.getFlexScheduleApplicationIdentifier(flexScheduleApplicationItem)!
      );
      const flexScheduleApplicationsToAdd = flexScheduleApplications.filter(flexScheduleApplicationItem => {
        const flexScheduleApplicationIdentifier = this.getFlexScheduleApplicationIdentifier(flexScheduleApplicationItem);
        if (flexScheduleApplicationCollectionIdentifiers.includes(flexScheduleApplicationIdentifier)) {
          return false;
        }
        flexScheduleApplicationCollectionIdentifiers.push(flexScheduleApplicationIdentifier);
        return true;
      });
      return [...flexScheduleApplicationsToAdd, ...flexScheduleApplicationCollection];
    }
    return flexScheduleApplicationCollection;
  }

  protected convertDateFromClient<T extends IFlexScheduleApplication | NewFlexScheduleApplication | PartialUpdateFlexScheduleApplication>(
    flexScheduleApplication: T
  ): RestOf<T> {
    return {
      ...flexScheduleApplication,
      effectiveFrom: flexScheduleApplication.effectiveFrom?.format(DATE_FORMAT) ?? null,
      effectiveTo: flexScheduleApplication.effectiveTo?.format(DATE_FORMAT) ?? null,
      sanctionedAt: flexScheduleApplication.sanctionedAt?.toJSON() ?? null,
      appliedAt: flexScheduleApplication.appliedAt?.format(DATE_FORMAT) ?? null,
      createdAt: flexScheduleApplication.createdAt?.toJSON() ?? null,
      updatedAt: flexScheduleApplication.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFlexScheduleApplication: RestFlexScheduleApplication): IFlexScheduleApplication {
    return {
      ...restFlexScheduleApplication,
      durationAsDays: calculateDurationOnDays(restFlexScheduleApplication.effectiveFrom, restFlexScheduleApplication.effectiveTo),
      effectiveFrom: restFlexScheduleApplication.effectiveFrom ? dayjs(restFlexScheduleApplication.effectiveFrom) : undefined,
      effectiveTo: restFlexScheduleApplication.effectiveTo ? dayjs(restFlexScheduleApplication.effectiveTo) : undefined,
      sanctionedAt: restFlexScheduleApplication.sanctionedAt ? dayjs(restFlexScheduleApplication.sanctionedAt) : undefined,
      appliedAt: restFlexScheduleApplication.appliedAt ? dayjs(restFlexScheduleApplication.appliedAt) : undefined,
      createdAt: restFlexScheduleApplication.createdAt ? dayjs(restFlexScheduleApplication.createdAt) : undefined,
      updatedAt: restFlexScheduleApplication.updatedAt ? dayjs(restFlexScheduleApplication.updatedAt) : undefined,
      inTime: restFlexScheduleApplication.inTime ? dayjs(restFlexScheduleApplication.inTime) : undefined,
      outTime: restFlexScheduleApplication.outTime ? dayjs(restFlexScheduleApplication.outTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFlexScheduleApplication>): HttpResponse<IFlexScheduleApplication> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFlexScheduleApplication[]>): HttpResponse<IFlexScheduleApplication[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
