drop index santri_data_name_uindex on santri_data;

create index santri_data_name_index
    on santri_data (name);

drop index santri_data_slug_uindex on santri_data;

create index santri_data_slug_index
    on santri_data (slug);
