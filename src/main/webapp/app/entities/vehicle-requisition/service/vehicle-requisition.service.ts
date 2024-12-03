import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleRequisition, NewVehicleRequisition } from '../vehicle-requisition.model';

export type PartialUpdateVehicleRequisition = Partial<IVehicleRequisition> & Pick<IVehicleRequisition, 'id'>;

type RestOf<T extends IVehicleRequisition | NewVehicleRequisition> = Omit<
  T,
  'createdAt' | 'updatedAt' | 'sanctionAt' | 'startDate' | 'endDate'
> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionAt?: string | null;
  startDate?: string | null;
  endDate?: string | null;
};

export type RestVehicleRequisition = RestOf<IVehicleRequisition>;

export type NewRestVehicleRequisition = RestOf<NewVehicleRequisition>;

export type PartialUpdateRestVehicleRequisition = RestOf<PartialUpdateVehicleRequisition>;

export type EntityResponseType = HttpResponse<IVehicleRequisition>;
export type EntityArrayResponseType = HttpResponse<IVehicleRequisition[]>;

@Injectable({ providedIn: 'root' })
export class VehicleRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicleRequisition: NewVehicleRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleRequisition);
    return this.http
      .post<RestVehicleRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vehicleRequisition: IVehicleRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleRequisition);
    return this.http
      .put<RestVehicleRequisition>(`${this.resourceUrl}/${this.getVehicleRequisitionIdentifier(vehicleRequisition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vehicleRequisition: PartialUpdateVehicleRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleRequisition);
    return this.http
      .patch<RestVehicleRequisition>(`${this.resourceUrl}/${this.getVehicleRequisitionIdentifier(vehicleRequisition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVehicleRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVehicleRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleRequisitionIdentifier(vehicleRequisition: Pick<IVehicleRequisition, 'id'>): number {
    return vehicleRequisition.id;
  }

  compareVehicleRequisition(o1: Pick<IVehicleRequisition, 'id'> | null, o2: Pick<IVehicleRequisition, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleRequisitionIdentifier(o1) === this.getVehicleRequisitionIdentifier(o2) : o1 === o2;
  }

  addVehicleRequisitionToCollectionIfMissing<Type extends Pick<IVehicleRequisition, 'id'>>(
    vehicleRequisitionCollection: Type[],
    ...vehicleRequisitionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleRequisitions: Type[] = vehicleRequisitionsToCheck.filter(isPresent);
    if (vehicleRequisitions.length > 0) {
      const vehicleRequisitionCollectionIdentifiers = vehicleRequisitionCollection.map(
        vehicleRequisitionItem => this.getVehicleRequisitionIdentifier(vehicleRequisitionItem)!
      );
      const vehicleRequisitionsToAdd = vehicleRequisitions.filter(vehicleRequisitionItem => {
        const vehicleRequisitionIdentifier = this.getVehicleRequisitionIdentifier(vehicleRequisitionItem);
        if (vehicleRequisitionCollectionIdentifiers.includes(vehicleRequisitionIdentifier)) {
          return false;
        }
        vehicleRequisitionCollectionIdentifiers.push(vehicleRequisitionIdentifier);
        return true;
      });
      return [...vehicleRequisitionsToAdd, ...vehicleRequisitionCollection];
    }
    return vehicleRequisitionCollection;
  }

  protected convertDateFromClient<T extends IVehicleRequisition | NewVehicleRequisition | PartialUpdateVehicleRequisition>(
    vehicleRequisition: T
  ): RestOf<T> {
    return {
      ...vehicleRequisition,
      createdAt: vehicleRequisition.createdAt?.toJSON() ?? null,
      updatedAt: vehicleRequisition.updatedAt?.toJSON() ?? null,
      sanctionAt: vehicleRequisition.sanctionAt?.toJSON() ?? null,
      startDate: vehicleRequisition.startDate?.format(DATE_FORMAT) ?? null,
      endDate: vehicleRequisition.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVehicleRequisition: RestVehicleRequisition): IVehicleRequisition {
    return {
      ...restVehicleRequisition,
      createdAt: restVehicleRequisition.createdAt ? dayjs(restVehicleRequisition.createdAt) : undefined,
      updatedAt: restVehicleRequisition.updatedAt ? dayjs(restVehicleRequisition.updatedAt) : undefined,
      sanctionAt: restVehicleRequisition.sanctionAt ? dayjs(restVehicleRequisition.sanctionAt) : undefined,
      startDate: restVehicleRequisition.startDate ? dayjs(restVehicleRequisition.startDate) : undefined,
      endDate: restVehicleRequisition.endDate ? dayjs(restVehicleRequisition.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVehicleRequisition>): HttpResponse<IVehicleRequisition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVehicleRequisition[]>): HttpResponse<IVehicleRequisition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
