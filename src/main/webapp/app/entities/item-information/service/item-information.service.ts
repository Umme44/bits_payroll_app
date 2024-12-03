import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemInformation, NewItemInformation } from '../item-information.model';
import { IDepartmentItems } from '../../department/department-items';

export type PartialUpdateItemInformation = Partial<IItemInformation> & Pick<IItemInformation, 'id'>;

type RestOf<T extends IItemInformation | NewItemInformation> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestItemInformation = RestOf<IItemInformation>;

export type NewRestItemInformation = RestOf<NewItemInformation>;

export type PartialUpdateRestItemInformation = RestOf<PartialUpdateItemInformation>;

export type EntityResponseType = HttpResponse<IItemInformation>;
export type EntityArrayResponseType = HttpResponse<IItemInformation[]>;

@Injectable({ providedIn: 'root' })
export class ItemInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/procurement-mgt/item-information');
  public resourceUrlCommon = SERVER_API_URL + 'api/common/item-information';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemInformation: NewItemInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemInformation);
    return this.http
      .post<RestItemInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(itemInformation: IItemInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemInformation);
    return this.http
      .put<RestItemInformation>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(itemInformation: PartialUpdateItemInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemInformation);
    return this.http
      .patch<RestItemInformation>(`${this.resourceUrl}/${this.getItemInformationIdentifier(itemInformation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestItemInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestItemInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getDepartmentsAndItemsMapping(): Observable<HttpResponse<IDepartmentItems[]>> {
    return this.http.get<IDepartmentItems[]>(`${this.resourceUrlCommon}/departments-and-items-mapping-list`, { observe: 'response' });
  }
  getByDepartmentId(departmentId: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestItemInformation[]>(`${this.resourceUrlCommon}/get-by-department-id/${departmentId}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getItemInformationIdentifier(itemInformation: Pick<IItemInformation, 'id'>): number {
    return itemInformation.id;
  }

  compareItemInformation(o1: Pick<IItemInformation, 'id'> | null, o2: Pick<IItemInformation, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemInformationIdentifier(o1) === this.getItemInformationIdentifier(o2) : o1 === o2;
  }

  addItemInformationToCollectionIfMissing<Type extends Pick<IItemInformation, 'id'>>(
    itemInformationCollection: Type[],
    ...itemInformationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const itemInformations: Type[] = itemInformationsToCheck.filter(isPresent);
    if (itemInformations.length > 0) {
      const itemInformationCollectionIdentifiers = itemInformationCollection.map(
        itemInformationItem => this.getItemInformationIdentifier(itemInformationItem)!
      );
      const itemInformationsToAdd = itemInformations.filter(itemInformationItem => {
        const itemInformationIdentifier = this.getItemInformationIdentifier(itemInformationItem);
        if (itemInformationCollectionIdentifiers.includes(itemInformationIdentifier)) {
          return false;
        }
        itemInformationCollectionIdentifiers.push(itemInformationIdentifier);
        return true;
      });
      return [...itemInformationsToAdd, ...itemInformationCollection];
    }
    return itemInformationCollection;
  }

  protected convertDateFromClient<T extends IItemInformation | NewItemInformation | PartialUpdateItemInformation>(
    itemInformation: T
  ): RestOf<T> {
    return {
      ...itemInformation,
      createdAt: itemInformation.createdAt?.toJSON() ?? null,
      updatedAt: itemInformation.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restItemInformation: RestItemInformation): IItemInformation {
    return {
      ...restItemInformation,
      createdAt: restItemInformation.createdAt ? dayjs(restItemInformation.createdAt) : undefined,
      updatedAt: restItemInformation.updatedAt ? dayjs(restItemInformation.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestItemInformation>): HttpResponse<IItemInformation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestItemInformation[]>): HttpResponse<IItemInformation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
