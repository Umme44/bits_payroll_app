import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUnitOfMeasurement, NewUnitOfMeasurement } from '../unit-of-measurement.model';

export type PartialUpdateUnitOfMeasurement = Partial<IUnitOfMeasurement> & Pick<IUnitOfMeasurement, 'id'>;

type RestOf<T extends IUnitOfMeasurement | NewUnitOfMeasurement> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestUnitOfMeasurement = RestOf<IUnitOfMeasurement>;

export type NewRestUnitOfMeasurement = RestOf<NewUnitOfMeasurement>;

export type PartialUpdateRestUnitOfMeasurement = RestOf<PartialUpdateUnitOfMeasurement>;

export type EntityResponseType = HttpResponse<IUnitOfMeasurement>;
export type EntityArrayResponseType = HttpResponse<IUnitOfMeasurement[]>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasurementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/procurement-mgt/unit-of-measurements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(unitOfMeasurement: NewUnitOfMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unitOfMeasurement);
    return this.http
      .post<RestUnitOfMeasurement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(unitOfMeasurement: IUnitOfMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unitOfMeasurement);
    return this.http
      .put<RestUnitOfMeasurement>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(unitOfMeasurement: PartialUpdateUnitOfMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unitOfMeasurement);
    return this.http
      .patch<RestUnitOfMeasurement>(`${this.resourceUrl}/${this.getUnitOfMeasurementIdentifier(unitOfMeasurement)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUnitOfMeasurement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUnitOfMeasurement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  checkNameIsUnique(name: string, id?: number): Observable<HttpResponse<boolean>> {
    if (id === undefined || id === null) {
      return this.http.get<boolean>(`${this.resourceUrl}/is-name-unique?name=${name}`, { observe: 'response' });
    }
    return this.http.get<boolean>(`${this.resourceUrl}/is-name-unique?name=${name}&id=${id}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUnitOfMeasurementIdentifier(unitOfMeasurement: Pick<IUnitOfMeasurement, 'id'>): number {
    return unitOfMeasurement.id;
  }

  compareUnitOfMeasurement(o1: Pick<IUnitOfMeasurement, 'id'> | null, o2: Pick<IUnitOfMeasurement, 'id'> | null): boolean {
    return o1 && o2 ? this.getUnitOfMeasurementIdentifier(o1) === this.getUnitOfMeasurementIdentifier(o2) : o1 === o2;
  }

  addUnitOfMeasurementToCollectionIfMissing<Type extends Pick<IUnitOfMeasurement, 'id'>>(
    unitOfMeasurementCollection: Type[],
    ...unitOfMeasurementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const unitOfMeasurements: Type[] = unitOfMeasurementsToCheck.filter(isPresent);
    if (unitOfMeasurements.length > 0) {
      const unitOfMeasurementCollectionIdentifiers = unitOfMeasurementCollection.map(
        unitOfMeasurementItem => this.getUnitOfMeasurementIdentifier(unitOfMeasurementItem)!
      );
      const unitOfMeasurementsToAdd = unitOfMeasurements.filter(unitOfMeasurementItem => {
        const unitOfMeasurementIdentifier = this.getUnitOfMeasurementIdentifier(unitOfMeasurementItem);
        if (unitOfMeasurementCollectionIdentifiers.includes(unitOfMeasurementIdentifier)) {
          return false;
        }
        unitOfMeasurementCollectionIdentifiers.push(unitOfMeasurementIdentifier);
        return true;
      });
      return [...unitOfMeasurementsToAdd, ...unitOfMeasurementCollection];
    }
    return unitOfMeasurementCollection;
  }

  protected convertDateFromClient<T extends IUnitOfMeasurement | NewUnitOfMeasurement | PartialUpdateUnitOfMeasurement>(
    unitOfMeasurement: T
  ): RestOf<T> {
    return {
      ...unitOfMeasurement,
      createdAt: unitOfMeasurement.createdAt?.toJSON() ?? null,
      updatedAt: unitOfMeasurement.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUnitOfMeasurement: RestUnitOfMeasurement): IUnitOfMeasurement {
    return {
      ...restUnitOfMeasurement,
      createdAt: restUnitOfMeasurement.createdAt ? dayjs(restUnitOfMeasurement.createdAt) : undefined,
      updatedAt: restUnitOfMeasurement.updatedAt ? dayjs(restUnitOfMeasurement.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUnitOfMeasurement>): HttpResponse<IUnitOfMeasurement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUnitOfMeasurement[]>): HttpResponse<IUnitOfMeasurement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
