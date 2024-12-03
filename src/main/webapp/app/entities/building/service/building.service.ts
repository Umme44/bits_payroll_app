import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBuilding, NewBuilding } from '../building.model';

export type PartialUpdateBuilding = Partial<IBuilding> & Pick<IBuilding, 'id'>;

type RestOf<T extends IBuilding | NewBuilding> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestBuilding = RestOf<IBuilding>;

export type NewRestBuilding = RestOf<NewBuilding>;

export type PartialUpdateRestBuilding = RestOf<PartialUpdateBuilding>;

export type EntityResponseType = HttpResponse<IBuilding>;
export type EntityArrayResponseType = HttpResponse<IBuilding[]>;

@Injectable({ providedIn: 'root' })
export class BuildingService {
  /* protected resourceUrl = this.applicationConfigService.getEndpointFor('api/buildings'); */
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/buildings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(building: NewBuilding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(building);
    return this.http
      .post<RestBuilding>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(building: IBuilding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(building);
    return this.http
      .put<RestBuilding>(`${this.resourceUrl}/${this.getBuildingIdentifier(building)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(building: PartialUpdateBuilding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(building);
    return this.http
      .patch<RestBuilding>(`${this.resourceUrl}/${this.getBuildingIdentifier(building)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBuilding>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBuilding[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBuildingIdentifier(building: Pick<IBuilding, 'id'>): number {
    return building.id;
  }

  compareBuilding(o1: Pick<IBuilding, 'id'> | null, o2: Pick<IBuilding, 'id'> | null): boolean {
    return o1 && o2 ? this.getBuildingIdentifier(o1) === this.getBuildingIdentifier(o2) : o1 === o2;
  }

  addBuildingToCollectionIfMissing<Type extends Pick<IBuilding, 'id'>>(
    buildingCollection: Type[],
    ...buildingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const buildings: Type[] = buildingsToCheck.filter(isPresent);
    if (buildings.length > 0) {
      const buildingCollectionIdentifiers = buildingCollection.map(buildingItem => this.getBuildingIdentifier(buildingItem)!);
      const buildingsToAdd = buildings.filter(buildingItem => {
        const buildingIdentifier = this.getBuildingIdentifier(buildingItem);
        if (buildingCollectionIdentifiers.includes(buildingIdentifier)) {
          return false;
        }
        buildingCollectionIdentifiers.push(buildingIdentifier);
        return true;
      });
      return [...buildingsToAdd, ...buildingCollection];
    }
    return buildingCollection;
  }

  protected convertDateFromClient<T extends IBuilding | NewBuilding | PartialUpdateBuilding>(building: T): RestOf<T> {
    return {
      ...building,
      createdAt: building.createdAt?.toJSON() ?? null,
      updatedAt: building.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBuilding: RestBuilding): IBuilding {
    return {
      ...restBuilding,
      createdAt: restBuilding.createdAt ? dayjs(restBuilding.createdAt) : undefined,
      updatedAt: restBuilding.updatedAt ? dayjs(restBuilding.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBuilding>): HttpResponse<IBuilding> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBuilding[]>): HttpResponse<IBuilding[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
