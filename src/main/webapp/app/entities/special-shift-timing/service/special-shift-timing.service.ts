import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpecialShiftTiming, NewSpecialShiftTiming } from '../special-shift-timing.model';

export type PartialUpdateSpecialShiftTiming = Partial<ISpecialShiftTiming> & Pick<ISpecialShiftTiming, 'id'>;

type RestOf<T extends ISpecialShiftTiming | NewSpecialShiftTiming> = Omit<T, 'startDate' | 'endDate' | 'createdAt' | 'updatedAt'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestSpecialShiftTiming = RestOf<ISpecialShiftTiming>;

export type NewRestSpecialShiftTiming = RestOf<NewSpecialShiftTiming>;

export type PartialUpdateRestSpecialShiftTiming = RestOf<PartialUpdateSpecialShiftTiming>;

export type EntityResponseType = HttpResponse<ISpecialShiftTiming>;
export type EntityArrayResponseType = HttpResponse<ISpecialShiftTiming[]>;

@Injectable({ providedIn: 'root' })
export class SpecialShiftTimingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/special-shift-timings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(specialShiftTiming: NewSpecialShiftTiming): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specialShiftTiming);
    return this.http
      .post<RestSpecialShiftTiming>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(specialShiftTiming: ISpecialShiftTiming): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specialShiftTiming);
    return this.http
      .put<RestSpecialShiftTiming>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(specialShiftTiming: PartialUpdateSpecialShiftTiming): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specialShiftTiming);
    return this.http
      .patch<RestSpecialShiftTiming>(`${this.resourceUrl}/${this.getSpecialShiftTimingIdentifier(specialShiftTiming)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSpecialShiftTiming>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSpecialShiftTiming[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSpecialShiftTimingIdentifier(specialShiftTiming: Pick<ISpecialShiftTiming, 'id'>): number {
    return specialShiftTiming.id;
  }

  compareSpecialShiftTiming(o1: Pick<ISpecialShiftTiming, 'id'> | null, o2: Pick<ISpecialShiftTiming, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpecialShiftTimingIdentifier(o1) === this.getSpecialShiftTimingIdentifier(o2) : o1 === o2;
  }

  addSpecialShiftTimingToCollectionIfMissing<Type extends Pick<ISpecialShiftTiming, 'id'>>(
    specialShiftTimingCollection: Type[],
    ...specialShiftTimingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const specialShiftTimings: Type[] = specialShiftTimingsToCheck.filter(isPresent);
    if (specialShiftTimings.length > 0) {
      const specialShiftTimingCollectionIdentifiers = specialShiftTimingCollection.map(
        specialShiftTimingItem => this.getSpecialShiftTimingIdentifier(specialShiftTimingItem)!
      );
      const specialShiftTimingsToAdd = specialShiftTimings.filter(specialShiftTimingItem => {
        const specialShiftTimingIdentifier = this.getSpecialShiftTimingIdentifier(specialShiftTimingItem);
        if (specialShiftTimingCollectionIdentifiers.includes(specialShiftTimingIdentifier)) {
          return false;
        }
        specialShiftTimingCollectionIdentifiers.push(specialShiftTimingIdentifier);
        return true;
      });
      return [...specialShiftTimingsToAdd, ...specialShiftTimingCollection];
    }
    return specialShiftTimingCollection;
  }

  protected convertDateFromClient<T extends ISpecialShiftTiming | NewSpecialShiftTiming | PartialUpdateSpecialShiftTiming>(
    specialShiftTiming: T
  ): RestOf<T> {
    return {
      ...specialShiftTiming,
      startDate: specialShiftTiming.startDate?.format(DATE_FORMAT) ?? null,
      endDate: specialShiftTiming.endDate?.format(DATE_FORMAT) ?? null,
      createdAt: specialShiftTiming.createdAt?.toJSON() ?? null,
      updatedAt: specialShiftTiming.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSpecialShiftTiming: RestSpecialShiftTiming): ISpecialShiftTiming {
    return {
      ...restSpecialShiftTiming,
      startDate: restSpecialShiftTiming.startDate ? dayjs(restSpecialShiftTiming.startDate) : undefined,
      endDate: restSpecialShiftTiming.endDate ? dayjs(restSpecialShiftTiming.endDate) : undefined,
      createdAt: restSpecialShiftTiming.createdAt ? dayjs(restSpecialShiftTiming.createdAt) : undefined,
      updatedAt: restSpecialShiftTiming.updatedAt ? dayjs(restSpecialShiftTiming.updatedAt) : undefined,
      inTime: restSpecialShiftTiming.inTime ? dayjs(restSpecialShiftTiming.inTime) : undefined,
      outTime: restSpecialShiftTiming.outTime ? dayjs(restSpecialShiftTiming.outTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSpecialShiftTiming>): HttpResponse<ISpecialShiftTiming> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSpecialShiftTiming[]>): HttpResponse<ISpecialShiftTiming[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
